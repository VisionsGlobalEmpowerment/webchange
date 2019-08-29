(ns webchange.interpreter.events
  (:require
    [re-frame.core :as re-frame]
    [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]
    [webchange.interpreter.core :as i]
    [webchange.interpreter.executor :as e]
    [webchange.common.events :as ce]
    [webchange.common.svg-path.path-to-transitions :as path-utils]
    [webchange.interpreter.variables.events :as vars.events]
    [webchange.common.anim :refer [start-animation]]
    [ajax.core :refer [json-request-format json-response-format]]
    ))

(re-frame/reg-fx
  :execute-audio
  (fn [{:keys [flow-id] :as params}]
    (e/init)
    (-> (e/execute-audio params)
        (.then (fn [audio] (re-frame/dispatch [::ce/register-flow-remove-handler {:flow-id flow-id :handler (fn [] (.stop audio))}]))))))

(re-frame/reg-fx
  :music-volume
  (fn [value]
    (e/init)
    (e/music-volume (/ value 100))))

(re-frame/reg-fx
  :effects-volume
  (fn [value]
    (e/effects-volume (/ value 100))))

(re-frame/reg-fx
  :load-course
  (fn [course-id]
    (i/load-course course-id (fn [course] (do (re-frame/dispatch [:complete-request :load-course])
                                              (re-frame/dispatch [::set-course-data course])
                                              (re-frame/dispatch [::load-progress course-id])
                                              (re-frame/dispatch [::load-lessons course-id])
                                              (re-frame/dispatch [::set-current-scene (:initial-scene course)]))))))

(re-frame/reg-fx
  :load-scene
  (fn [[course-id scene-id]]
    (i/load-scene course-id scene-id (fn [scene] (re-frame/dispatch [::set-scene scene-id scene])))))

(re-frame/reg-fx
  :load-progress
  (fn [course-id]
    (i/load-progress course-id (fn [progress]
                                 (re-frame/dispatch [:complete-request :load-progress])
                                 (if progress
                                   (re-frame/dispatch [::set-progress-data progress])
                                   (re-frame/dispatch [::next-workflow-action]))
                                 (re-frame/dispatch [::progress-loaded])
                                 (re-frame/dispatch [::check-course-loaded])))))

(re-frame/reg-fx
  :load-lessons
  (fn [course-id]
    (i/load-lessons course-id
                    (fn [{:keys [items lesson-sets]}]
                      (re-frame/dispatch [:complete-request :load-lessons])
                      (re-frame/dispatch [::set-course-dataset-items items])
                      (re-frame/dispatch [::set-course-lessons lesson-sets])
                      (re-frame/dispatch [::check-course-loaded]))
                    #(re-frame/dispatch [::set-dataset-loading-progress %])
                    #(do (re-frame/dispatch [::set-dataset-loaded])
                         (re-frame/dispatch [::check-course-loaded])))))

(re-frame/reg-fx
  :reload-asset
  (fn [asset]
    (i/load-asset asset)))

(re-frame/reg-fx
  :transition
  (fn [params]
    (i/interpolate params)))

(re-frame/reg-fx
  :switch-animation
  (fn [{:keys [state id track] :or {track 0} :as action}]
    (let [loop (if (contains? action :loop) (:loop action) true)]
      (.setAnimation (:animation-state state) track id loop))))

(re-frame/reg-fx
  :add-animation
  (fn [{:keys [state id track] :or {track 0} :as action}]
    (let [loop (if (contains? action :loop) (:loop action) true)]
      (.addAnimation (:animation-state state) track id loop 0))))

(re-frame/reg-fx
  :remove-animation
  (fn [{:keys [state track] :or {track 0} :as action}]
    (.setEmptyAnimation (:animation-state state) track 0.2)))

(re-frame/reg-fx
  :start-animation
  (fn [shape]
    (start-animation shape)))

(re-frame/reg-fx
  :set-skin
  (fn [{:keys [state skin]}]
    (.setSkinByName (:skeleton state) skin)))

(re-frame/reg-fx
  :animation-props
  (fn [{{skeleton :skeleton} :state {:keys [scaleX scaleY x y]} :props}]
    (when scaleX (set! (.-scaleX skeleton) scaleX))
    (when scaleY (set! (.-scaleY skeleton) scaleY))
    (when x (set! (.-x skeleton) x))
    (when y (set! (.-y skeleton) y))))

(defn get-audio-key
  [db id]
  (get-in db [:scenes (:current-scene db) :audio (keyword id)]))

(defn workflow-action
  [db id]
  (let [actions (get-in db [:course-data :workflow-actions])]
    (->> actions
         (filter #(= id (:id %)))
         first)))

(defn first-nonfinished-action [db]
  (let [workflow-actions (get-in db [:course-data :workflow-actions])
        finished-actions (set (get-in db [:progress-data :finished-workflow-actions]))]
    (->> workflow-actions
         (remove #(contains? finished-actions (:id %)))
         first)))

(defn first-nonfinished-action-by-name [db name]
  (let [workflow-actions (get-in db [:course-data :workflow-actions])
        finished-actions (set (get-in db [:progress-data :finished-workflow-actions]))]
    (->> workflow-actions
         (remove #(contains? finished-actions (:id %)))
         (filter #(= name (:activity %)))
         first)))

(ce/reg-simple-executor :audio ::execute-audio)
(ce/reg-simple-executor :play-video ::play-video)
(ce/reg-simple-executor :path-animation ::execute-path-animation)
(ce/reg-simple-executor :state ::execute-state)
(ce/reg-simple-executor :set-attribute ::execute-set-attribute)
(ce/reg-simple-executor :add-alias ::execute-add-alias)
(ce/reg-simple-executor :empty ::execute-empty)
(ce/reg-simple-executor :animation ::execute-animation)
(ce/reg-simple-executor :add-animation ::execute-add-animation)
(ce/reg-simple-executor :start-animation ::execute-start-animation)
(ce/reg-simple-executor :remove-animation ::execute-remove-animation)
(ce/reg-simple-executor :set-skin ::execute-set-skin)
(ce/reg-simple-executor :animation-props ::execute-set-animation-props)
(ce/reg-simple-executor :animation-sequence ::execute-animation-sequence)
(ce/reg-simple-executor :scene ::execute-scene)
(ce/reg-simple-executor :transition ::execute-transition)
(ce/reg-simple-executor :placeholder-audio ::execute-placeholder-audio)
(ce/reg-simple-executor :test-transitions-collide ::execute-test-transitions-collide)
(ce/reg-simple-executor :start-activity ::execute-start-activity)
(ce/reg-simple-executor :stop-activity ::execute-stop-activity)
(ce/reg-simple-executor :finish-activity ::execute-finish-activity)
(ce/reg-simple-executor :text-animation ::execute-text-animation)
(ce/reg-simple-executor :pick-correct ::execute-pick-correct)
(ce/reg-simple-executor :pick-wrong ::execute-pick-wrong)
(ce/reg-simple-executor :set-current-concept ::execute-set-current-concept)

(re-frame/reg-event-fx
  ::execute-placeholder-audio
  (fn [{:keys [db]} [_ {:keys [var-name] :as action}]]
    (let [scene-id (:current-scene db)
          scene (get-in db [:scenes scene-id])
          variable (get-in scene [:variables var-name])
          audio-params (-> action
                           (assoc :key (get-audio-key db (get variable (-> action :id keyword))))
                           (assoc :start (get variable (-> action :start keyword)))
                           (assoc :duration (get variable (-> action :duration keyword)))
                           (assoc :offset (get variable (-> action :offset keyword))))]
      {:execute-audio (-> audio-params
                          (assoc :on-ended (ce/dispatch-success-fn action)))})))

(defn execute-transition
  [db {:keys [transition-id to] :as action}]
  (let [scene-id (:current-scene db)
        transition (get-in db [:transitions scene-id transition-id])]
    {:transition {:component transition
                  :to        to
                  :on-ended  (ce/dispatch-success-fn action)}}))

(defn execute-transitions-sequence
  [transitions {:keys [transition-id] :as action}]
  (let [data (map (fn [transition] {:type          "transition"
                                    :transition-id transition-id
                                    :to            transition}) transitions)]
    {:dispatch [::ce/execute-sequence-data (merge action {:data data})]}))

(re-frame/reg-event-fx
  ::execute-transition
  (fn [{:keys [db]} [_ {:keys [to] :as action}]]
    (if (:path to)
      (execute-transitions-sequence (path-utils/path->transitions to) action)
      (execute-transition db action))))

(re-frame/reg-event-fx
  ::execute-scene
  (fn [{:keys [db]} [_ {:keys [scene-id] :as action}]]
    {:dispatch-n (list [::set-current-scene scene-id] (ce/success-event action))}))

(re-frame/reg-event-fx
  ::execute-audio
  [ce/event-as-action ce/with-flow]
  (fn [{:keys [db]} {:keys [id] :as action}]
    {:execute-audio (-> action
                        (assoc :key (or (get-audio-key db id) id))
                        (assoc :on-ended (ce/dispatch-success-fn action)))}))

(re-frame/reg-event-fx
  ::play-video
  [ce/event-as-action ce/with-flow]
  (fn [{:keys [db]} {:keys [target src params flow-id] :as action}]
    (let [scene-id (:current-scene db)
          on-end (ce/dispatch-success-fn action)
          video-state {:act    "play"
                       :src    src
                       :on-end on-end}]
      {:db       (update-in db [:scenes scene-id :objects (keyword target)] merge video-state params)
       :dispatch [::ce/register-flow-remove-handler {:flow-id flow-id :handler (fn [] (re-frame/dispatch [::stop-video {:target target}]))}]})))

(re-frame/reg-event-fx
  ::stop-video
  [ce/event-as-action]
  (fn [{:keys [db]} {:keys [target params]}]
    (let [scene-id (:current-scene db)
          video-state {:act "pause"}]
      {:db (update-in db [:scenes scene-id :objects (keyword target)] merge video-state params)})))

(re-frame/reg-event-fx
  ::execute-path-animation
  [ce/event-as-action ce/with-flow]
  (fn [{:keys [db]} {:keys [target params state flow-id] :as action}]
    (let [scene-id (:current-scene db)
          on-end (ce/dispatch-success-fn action)
          path-state {:animation state
                      :on-end    on-end}]
      {:db       (update-in db [:scenes scene-id :objects (keyword target)] merge path-state params)
       :dispatch [::ce/register-flow-remove-handler {:flow-id flow-id
                                                     :handler (fn [] (re-frame/dispatch [::stop-path-animation {:target target}]))}]})))

(re-frame/reg-event-fx
  ::stop-path-animation
  [ce/event-as-action]
  (fn [{:keys [db]} {:keys [target params]}]
    (let [scene-id (:current-scene db)
          path-state {:animation "stop"}]
      {:db (update-in db [:scenes scene-id :objects (keyword target)] merge path-state params)})))

(re-frame/reg-event-fx
  ::execute-add-alias
  (fn [{:keys [db]} [_ {:keys [target alias state] :as action}]]
    (let [scene-id (:current-scene db)]
      {:db       (assoc-in db [:scenes scene-id :objects (keyword target) :states-aliases (keyword alias)] state)
       :dispatch (ce/success-event action)})))

(re-frame/reg-event-fx
  ::execute-state
  (fn [{:keys [db]} [_ {:keys [target id params] :as action}]]
    (let [scene-id (:current-scene db)
          scene (get-in db [:scenes scene-id])
          object (get-in scene [:objects (keyword target)])
          states (get object :states)
          states-with-aliases (reduce-kv (fn [m k v] (assoc m k (get states (keyword v)))) states (get object :states-aliases))
          state (get states-with-aliases (keyword id))]
      {:db       (update-in db [:scenes scene-id :objects (keyword target)] merge state params)
       :dispatch (ce/success-event action)})))

(re-frame/reg-event-fx
  ::execute-set-attribute
  (fn [{:keys [db]} [_ {:keys [target attr-name attr-value params] :as action}]]
    (let [scene-id (:current-scene db)
          patch (into {} [[(keyword attr-name) attr-value]])]
      {:db       (update-in db [:scenes scene-id :objects (keyword target)] merge patch params)
       :dispatch (ce/success-event action)})))

(re-frame/reg-event-fx
  ::execute-empty
  (fn [{:keys [db]} [_ action]]
    {:dispatch-later [{:ms (:duration action) :dispatch (ce/success-event action)}]}))

(re-frame/reg-event-fx
  ::execute-animation
  (fn [{:keys [db]} [_ action]]
    (let [scene-id (:current-scene db)]
      {:switch-animation (-> action
                             (assoc :state (get-in db [:scenes scene-id :animations (:target action)])))
       :dispatch-n       (list (ce/success-event action))})))

(re-frame/reg-event-fx
  ::execute-animation-sequence
  (fn [{:keys [db]} [_ action]]
    (let [animation-actions (i/animation-sequence->actions action)
          audio-action (i/animation-sequence->audio-action action)]
      (if audio-action
        {:dispatch [::ce/execute-parallel (assoc action :data (conj animation-actions audio-action))]}
        {:dispatch [::ce/execute-parallel (assoc action :data animation-actions)]})
      )))


(re-frame/reg-event-fx
  ::execute-add-animation
  (fn [{:keys [db]} [_ action]]
    (let [scene-id (:current-scene db)]
      {:add-animation (-> action
                          (assoc :state (get-in db [:scenes scene-id :animations (:target action)])))
       :dispatch-n    (list (ce/success-event action))})))

(re-frame/reg-event-fx
  ::execute-start-animation
  (fn [{:keys [db]} [_ action]]
    (let [scene-id (:current-scene db)]
      {:start-animation (-> db (get-in [:scenes scene-id :animations (:target action)]) :shape)
       :dispatch-n      (list (ce/success-event action))})))

(re-frame/reg-event-fx
  ::execute-remove-animation
  (fn [{:keys [db]} [_ action]]
    (let [scene-id (:current-scene db)]
      {:remove-animation (-> action
                             (assoc :state (get-in db [:scenes scene-id :animations (:target action)])))
       :dispatch-n       (list (ce/success-event action))})))

(re-frame/reg-event-fx
  ::execute-set-skin
  (fn [{:keys [db]} [_ action]]
    (let [scene-id (:current-scene db)]
      {:set-skin   (-> action
                       (assoc :state (get-in db [:scenes scene-id :animations (:target action)])))
       :dispatch-n (list (ce/success-event action))})))

(re-frame/reg-event-fx
  ::execute-set-animation-props
  (fn [{:keys [db]} [_ action]]
    (let [scene-id (:current-scene db)]
      {:animation-props (-> action
                            (assoc :state (get-in db [:scenes scene-id :animations (:target action)])))
       :dispatch-n      (list (ce/success-event action))})))

(defn name->activity-action-id
  [db scene-name]
  (-> db
      (get-in [:progress-data :scene-activities])
      (get (keyword scene-name))
      (or (-> (first-nonfinished-action-by-name db scene-name) :id))))

(re-frame/reg-event-fx
  ::execute-start-activity
  (fn [{:keys [db]} [_ {activity-name :id :as action}]]
    (let [activity-action-id (name->activity-action-id db activity-name)
          lesson (-> (workflow-action db activity-action-id) :lesson)]
      {:db         (assoc db :activity-started true :activity-start-time (js/Date.) :activity-lesson lesson)
       :dispatch-n (list
                     [::add-pending-event :activity-started {:activity-id   activity-action-id
                                                             :activity-name activity-name
                                                             :lesson        lesson}]
                     (ce/success-event action))})))

(re-frame/reg-event-fx
  ::execute-stop-activity
  (fn [{:keys [db]} [_ {activity-name :id :as action}]]
    (let [current-lesson (:activity-lesson db)
          activity-action-id (name->activity-action-id db activity-name)
          start-time (get db :activity-start-time)
          time-spent (if start-time
                       (- (js/Date.) (get db :activity-start-time))
                       0)
          activity-started? (:activity-started db)]
      (if activity-started?
        {:db         (assoc db :activity-started false)
         :dispatch-n (list
                       [::add-pending-event :activity-stopped {:activity-id   activity-action-id
                                                               :activity-name activity-name
                                                               :lesson        current-lesson
                                                               :time-spent    time-spent}]
                       (ce/success-event action))}
        {:dispatch (ce/success-event action)}))))

(defn action-id->activity-number [action-id db]
  (->> (get-in db [:course-data :workflow-actions])
       (filter #(= action-id (:id %)))
       first
       :activity-number))

(defn activity-score
  [db]
  {:correct   (vars.events/get-variable db :score-correct)
   :incorrect (vars.events/get-variable db :score-incorrect)
   :mistake   (vars.events/get-variable db :score-mistake)})

(defn activity-score-percentage
  [db]
  (let [score (activity-score db)
        has-score? (:correct score)]
    (if has-score?
      (-> (- (:correct score) (:incorrect score))
          (/ (:correct score))
          (* 100)
          js/Math.round)
      100)))

(defn activity-finished-event [db {activity-name :id}]
  (let [current-lesson (:activity-lesson db)
        activity-action-id (name->activity-action-id db activity-name)
        activity-number (action-id->activity-number activity-action-id db)
        score (activity-score db)
        start-time (get db :activity-start-time)
        time-spent (if start-time
                     (- (js/Date.) (get db :activity-start-time))
                     0)]
    [::add-pending-event :activity-finished {:activity-id     activity-action-id
                                             :activity-name   activity-name
                                             :lesson          current-lesson
                                             :score           score
                                             :activity-number activity-number
                                             :time-spent      time-spent}]))

(defn workflow-action-finished?
  [db {activity-name :id}]
  (let [workflow-action-id (get-in db [:progress-data :current-workflow-action])
        activity-action-id (name->activity-action-id db activity-name)
        current-activity? (= workflow-action-id activity-action-id)

        score-percentage (-> (workflow-action db activity-action-id) :expected-score-percentage)
        score-passed? (if score-percentage
                        (> (activity-score-percentage db) score-percentage)
                        true)]
    (and current-activity? score-passed?)))

(re-frame/reg-event-fx
  ::execute-finish-activity
  (fn [{:keys [db]} [_ action]]
    (let [events (cond-> (list (ce/success-event action))
                         (workflow-action-finished? db action) (conj [::finish-workflow-action])
                         :always (conj (activity-finished-event db action)))
          activity-started? (:activity-started db)]
      (if activity-started?
        {:db         (assoc db :activity-started false)
         :dispatch-n events}
        {:dispatch (ce/success-event action)}))))

(re-frame/reg-event-fx
  ::set-scene-activities
  (fn [{:keys [db]} [_ {scene-activities :scene-activities}]]
    {:db       (assoc-in db [:progress-data :scene-activities] scene-activities)
     :dispatch [::finish-workflow-action]}))

(defn next-action->event [action]
  (case (:type action)
    "set-activity" [::set-activity action]
    "init-progress" [::init-default-progress]
    "set-scene-activities" [::set-scene-activities action]
    nil))

(re-frame/reg-event-fx
  ::finish-workflow-action
  (fn [{:keys [db]} _]
    (let [current-workflow-action (get-in db [:progress-data :current-workflow-action])
          next (-> (first-nonfinished-action db) :id)
          action-id (or current-workflow-action next)]
      {:db         (update-in db [:progress-data :finished-workflow-actions] #(-> % set (conj action-id)))
       :dispatch-n (list [::next-workflow-action] [:progress-data-changed])})))

(re-frame/reg-event-fx
  ::next-workflow-action
  (fn [{:keys [db]} _]
    (let [next-action (first-nonfinished-action db)]
      (if next-action
        {:db       (assoc-in db [:progress-data :current-workflow-action] (:id next-action))
         :dispatch (next-action->event next-action)}))))

(re-frame/reg-event-fx
  ::set-activity
  (fn [{:keys [db]} [_ action]]
    {:db       (assoc-in db [:progress-data :current-activity] (:activity action))
     :dispatch [:progress-data-changed]}))

(re-frame/reg-event-fx
  :progress-data-changed
  (fn [{:keys [db]} _]
    (let [course-id (:current-course db)
          progress (:progress-data db)
          events (:pending-events db)
          loading? (get-in db [:loading :save-progress])]
      (if loading?
        {:db (assoc db :schedule-save-progress true)}
        {:db         (-> db
                         (assoc-in [:loading :save-progress] true)
                         (dissoc :pending-events))
         :http-xhrio {:method          :post
                      :uri             (str "/api/courses/" course-id "/current-progress")
                      :params          {:progress progress :events events}
                      :format          (json-request-format)
                      :response-format (json-response-format {:keywords? true})
                      :on-success      [::save-progress-success]
                      :on-failure      [:api-request-error :save-progress]}}))))

(re-frame/reg-event-fx
  ::save-progress-success
  (fn [{:keys [db]} _]
    (let [scheduled? (:schedule-save-progress db)]
      (if scheduled?
        {:db         (assoc db :schedule-save-progress false)
         :dispatch-n (list [:complete-request :save-progress] [:progress-data-changed])}
        {:dispatch-n (list [:complete-request :save-progress])}))))

(re-frame/reg-event-fx
  ::set-music-volume
  (fn [{:keys [db]} [_ value]]
    (when value
      {:db (assoc-in db [:settings :music-volume] value)
       :music-volume value})))

(re-frame/reg-event-fx
  ::set-effects-volume
  (fn [{:keys [db]} [_ value]]
    (when value
      {:db (assoc-in db [:settings :effects-volume] value)
       :effects-volume value})))

(re-frame/reg-event-fx
  ::start-course
  (fn-traced [{:keys [db]} [_ course-id]]
    (if (not= course-id (:loaded-course db))
      {:db          (-> db
                        (assoc :loaded-course course-id)
                        (assoc :current-course course-id)
                        (assoc :ui-screen :course-loading)
                        (assoc-in [:loading :load-course] true))
       :load-course course-id})))

(re-frame/reg-event-fx
  ::set-current-course
  (fn [{:keys [db]} [_ course-name]]
    {:db (assoc db :current-course course-name)}))

(re-frame/reg-event-fx
  ::load-scene
  (fn [{:keys [db]} [_ scene-id]]
    (let [loaded (get-in db [:scene-loading-complete scene-id])]
      (when (not loaded) {:load-scene [(:current-course db) scene-id]}))))

(re-frame/reg-event-fx
  ::set-current-scene
  (fn [{:keys [db]} [_ scene-id]]
    (let [current-scene (:current-scene db)]
      {:db         (-> db
                       (assoc :current-scene scene-id)
                       (assoc :current-scene-data (get-in db [:scenes scene-id]))
                       (assoc :scene-started false))
       :dispatch-n (list [::vars.events/execute-clear-vars]
                         [::ce/execute-remove-flows {:flow-tag (str "scene-" current-scene)}]
                         [::reset-navigation]
                         [::reset-activity-action]
                         [::load-scene scene-id])})))

(re-frame/reg-event-fx
  ::set-course-data
  (fn [{:keys [db]} [_ course]]
    {:db (assoc db :course-data course)}))

(re-frame/reg-event-fx
  ::set-scene
  (fn [{:keys [db]} [_ scene-id scene]]
    (let [current-scene (:current-scene db)]
      {:db (cond-> (assoc-in db [:scenes scene-id] scene)
                   (= current-scene scene-id) (assoc :current-scene-data scene))})))

(re-frame/reg-event-fx
  ::set-progress-data
  (fn [{:keys [db]} [_ data]]
    {:db (assoc db :progress-data data)}))

(re-frame/reg-event-fx
  ::init-default-progress
  (fn [{:keys [db]} [_ _]]
    (let [default-progress (get-in db [:course-data :default-progress])]
      {:db       (update-in db [:progress-data] merge default-progress)
       :dispatch [::finish-workflow-action]})))

(re-frame/reg-event-db
  ::register-transition
  (fn [db [_ name component]]
    (let [scene-id (:current-scene db)]
      (assoc-in db [:transitions scene-id name] component))))

(re-frame/reg-event-db
  ::register-animation
  (fn [db [_ name animation]]
    (let [scene-id (:current-scene db)]
      (assoc-in db [:scenes scene-id :animations name] animation))))


(re-frame/reg-event-fx
  ::trigger
  (fn [{:keys [db]} [_ trigger]]
    (let [scene-id (:current-scene db)
          scene (get-in db [:scenes scene-id])
          actions (->> (:triggers scene)
                       (filter #(= trigger (-> % second :on keyword)))
                       (map second)
                       (map #(-> % :action keyword))
                       (map #(get-in scene [:actions %]))
                       (map (fn [action] [::ce/execute-action action])))]
      {:dispatch-n actions})))

(re-frame/reg-event-fx
  ::next-scene
  (fn [{:keys [db]} [_ _]]
    (let [scene-id (:current-scene db)
          scene (get-in db [:scenes scene-id])
          next (get-in scene [:metadata :next])]
      {:dispatch [::set-current-scene next]})))

(re-frame/reg-event-fx
  ::restart-scene
  (fn [{:keys [db]} [_ _]]
    (let [scene-id (:current-scene db)]
      {:dispatch [::set-current-scene scene-id]})))

(re-frame/reg-event-fx
  ::close-scene
  (fn [{:keys [db]} [_ _]]
    (let [scene-id (:current-scene db)
          scene (get-in db [:scenes scene-id])
          prev (get-in scene [:metadata :prev] nil)]
      (if prev
        {:dispatch-n (list [::trigger :back] [::set-current-scene prev])}
        {:dispatch [:open-student-dashboard]}))))

(re-frame/reg-event-fx
  ::open-student-dashboard
  (fn [{:keys [db]} [_ _]]
    (let [current-scene (:current-scene db)]
      {:dispatch-n (list [::ce/execute-remove-flows {:flow-tag (str "scene-" current-scene)}]
                         [:open-student-dashboard])})))

(re-frame/reg-event-fx
  ::load-progress
  (fn [{:keys [db]} [_ course-id]]
    {:db            (assoc-in db [:loading :load-progress] true)
     :load-progress course-id}))

(re-frame/reg-event-fx
  ::load-lessons
  (fn [{:keys [db]} [_ course-id]]
    {:db           (-> db
                       (assoc-in [:loading :load-lessons] true)
                       (assoc-in [:loading :load-lessons-assets] true))
     :load-lessons course-id}))

(re-frame/reg-event-fx
  ::show-scene-loading
  (fn [{:keys [db]} _]
    {:db (assoc db :ui-screen :default)}))

(re-frame/reg-event-fx
  ::check-course-loaded
  (fn [{:keys [db]} _]
    (let [loading (:loading db)]
      (if (some #(get loading %) [:load-course :load-progress :load-lessons :load-lessons-assets])
        {:db (assoc db :ui-screen :course-loading)}
        {:db (assoc db :ui-screen :default)}))))

(re-frame/reg-event-fx
  ::set-course-dataset-items
  (fn [{:keys [db]} [_ data]]
    (let [prepared (into {} (map #(identity [(:id %) %]) data))]
      {:db (assoc db :dataset-items prepared)})))

(defn prepare-lesson [{data :data :as lesson}]
  (assoc lesson :item-ids (map #(:id %) (:items data))))

(re-frame/reg-event-fx
  ::set-course-lessons
  (fn [{:keys [db]} [_ data]]
    (let [prepared (into {} (map #(identity [(:name %) (prepare-lesson %)]) data))]
      {:db (assoc db :lessons prepared)})))

(re-frame/reg-event-db
  ::set-dataset-loading-progress
  (fn [db [_ value]]
    (assoc db :dataset-loading-progress value)))

(re-frame/reg-event-db
  ::set-dataset-loaded
  (fn [db _]
    (assoc-in db [:loading :load-lessons-assets] false)))


(re-frame/reg-event-fx
  ::execute-test-transitions-collide
  (fn [{:keys [db]} [_ {:keys [transition-1 transition-2 success fail] :as action}]]
    (let [scene-id (:current-scene db)
          transition-1-shape (get-in db [:transitions scene-id transition-1])
          transition-2-shape (get-in db [:transitions scene-id transition-2])
          success (ce/get-action success db action)
          fail (ce/get-action fail db action)]
      (if (i/collide? transition-1-shape transition-2-shape)
        {:dispatch-n (list [::ce/execute-action success] (ce/success-event action))}
        {:dispatch-n (list [::ce/execute-action fail] (ce/success-event action))}))))

(re-frame/reg-event-fx
  ::execute-text-animation
  (fn [{:keys [db]} [_ action]]
    (let [animation-actions (i/text-animation-sequence->actions action)
          audio-action (i/animation-sequence->audio-action action)]
      (if audio-action
        {:dispatch [::ce/execute-parallel (assoc action :data (conj animation-actions audio-action))]}
        {:dispatch [::ce/execute-parallel (assoc action :data animation-actions)]})
      )))

(re-frame/reg-event-db
  ::reset-navigation
  (fn [db _]
    (let [current-activity (get-in db [:progress-data :current-activity])
          current-scene (get-in db [:current-scene])
          scene-list (get-in db [:course-data :scene-list])]
      (if (= current-activity current-scene)
        (dissoc db :navigation)
        (assoc db :navigation (i/find-exit-position current-scene current-activity scene-list))))))

(re-frame/reg-event-fx
  ::progress-loaded
  (fn [{:keys [db]} _]
    {:dispatch-n (list [::reset-navigation] [::reset-activity-action] [::load-settings])}))

(re-frame/reg-event-fx
  ::add-pending-event
  (fn [{:keys [db]} [_ type data]]
    (let [event (-> (or data {})
                    (assoc :id (-> (random-uuid) str))
                    (assoc :type type)
                    (assoc :created-at (-> (js/Date.) .toISOString)))]
      {:db       (update-in db [:pending-events] conj event)
       :dispatch [:progress-data-changed]})))

(re-frame/reg-event-fx
  ::start-playing
  (fn [{:keys [db]} _]
    {:db       (-> db
                   (assoc :playing true)
                   (assoc :scene-started true))
     :dispatch [::add-pending-event :course-started]}))

(re-frame/reg-event-fx
  ::execute-pick-correct
  (fn [{:keys [db]} [_ {:keys [activity concept-name] :as action}]]
    (let [current-lesson (:activity-lesson db)
          counter-value (or (vars.events/get-variable db :score-correct) 0)]
      {:db         (-> db
                       (vars.events/set-variable :score-correct (inc counter-value))
                       (vars.events/set-variable :score-first-attempt false))
       :dispatch-n (list
                     [::add-pending-event :concept-picked-correct {:activity activity :concept-name concept-name :lesson current-lesson}]
                     (ce/success-event action))})))

(re-frame/reg-event-fx
  ::execute-pick-wrong
  (fn [{:keys [db]} [_ {:keys [activity concept-name option] :as action}]]
    (let [current-lesson (:activity-lesson db)
          counter-incorrect (or (vars.events/get-variable db :score-incorrect) 0)
          counter-mistake (or (vars.events/get-variable db :score-mistake) 0)
          first-attempt? (vars.events/get-variable db :score-first-attempt)]
      {:db         (cond-> db
                           first-attempt? (vars.events/set-variable :score-incorrect (inc counter-incorrect))
                           :always (vars.events/set-variable :score-mistake (inc counter-mistake))
                           :always (vars.events/set-variable :score-first-attempt false))
       :dispatch-n (list
                     [::add-pending-event :concept-picked-wrong {:activity activity :concept-name concept-name :option option :lesson current-lesson}]
                     (ce/success-event action))})))

(re-frame/reg-event-fx
  ::execute-set-current-concept
  (fn [{:keys [db]} [_ {:keys [value] :as action}]]
    {:db         (vars.events/set-variable db :score-first-attempt true)
     :dispatch-n (list (ce/success-event action))}))

(re-frame/reg-event-db
  ::reset-activity-action
  (fn [db _]
    (let [scene-activities (get-in db [:progress-data :scene-activities])
          current-scene (get-in db [:current-scene])
          activity-action (get scene-activities (keyword current-scene))]
      (assoc db :activity-action activity-action))))

(re-frame/reg-event-db
  ::open-settings
  (fn [db _]
    (assoc db :ui-screen :settings)))

(re-frame/reg-event-fx
  ::close-settings
  (fn [{:keys [db]} _]
    {:db (assoc db :ui-screen :default)
     :dispatch [::save-settings]}))

(re-frame/reg-event-fx
  ::save-settings
  (fn [{:keys [db]} _]
    (let [settings (:settings db)]
      {:db (assoc-in db [:progress-data :settings] settings)
       :dispatch [:progress-data-changed]})))

(re-frame/reg-event-fx
  ::load-settings
  (fn [{:keys [db]} _]
    (let [{:keys [music-volume effects-volume] :as settings} (get-in db [:progress-data :settings])]
      {:db (update db :settings merge settings)
       :dispatch-n (list [::set-music-volume music-volume]
                         [::set-effects-volume effects-volume])})))