(ns webchange.interpreter.events
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.core :as i]
    [webchange.interpreter.executor :as e]
    [webchange.common.events :as ce]
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
                                   (re-frame/dispatch [::init-default-progress]))
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

(ce/reg-simple-executor :audio ::execute-audio)
(ce/reg-simple-executor :state ::execute-state)
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
(ce/reg-simple-executor :finish-activity ::execute-finish-activity)

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

(re-frame/reg-event-fx
  ::execute-transition
  (fn [{:keys [db]} [_ {:keys [transition-id to] :as action}]]
    (let [scene-id (:current-scene db)
          transition (get-in db [:transitions scene-id transition-id])]
      {:transition {:component transition
                    :to        to
                    :on-ended  (ce/dispatch-success-fn action)}})))

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
  ::execute-add-alias
  (fn [{:keys [db]} [_ {:keys [target alias state] :as action}]]
    (let [scene-id (:current-scene db)]
      {:db (assoc-in db [:scenes scene-id :objects (keyword target) :states-aliases (keyword alias)] state)
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
      {:db (update-in db [:scenes scene-id :objects (keyword target)] merge state params)
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
       :dispatch-n (list (ce/success-event action))})))

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
       :dispatch-n (list (ce/success-event action))})))

(re-frame/reg-event-fx
  ::execute-start-animation
  (fn [{:keys [db]} [_ action]]
    (let [scene-id (:current-scene db)]
      {:start-animation (-> db (get-in [:scenes scene-id :animations (:target action)]) :shape)
       :dispatch-n (list (ce/success-event action))})))

(re-frame/reg-event-fx
  ::execute-remove-animation
  (fn [{:keys [db]} [_ action]]
    (let [scene-id (:current-scene db)]
      {:remove-animation (-> action
                             (assoc :state (get-in db [:scenes scene-id :animations (:target action)])))
       :dispatch-n (list (ce/success-event action))})))

(re-frame/reg-event-fx
  ::execute-set-skin
  (fn [{:keys [db]} [_ action]]
    (let [scene-id (:current-scene db)]
      {:set-skin (-> action
                          (assoc :state (get-in db [:scenes scene-id :animations (:target action)])))
       :dispatch-n (list (ce/success-event action))})))

(re-frame/reg-event-fx
  ::execute-set-animation-props
  (fn [{:keys [db]} [_ action]]
    (let [scene-id (:current-scene db)]
      {:animation-props (-> action
                     (assoc :state (get-in db [:scenes scene-id :animations (:target action)])))
       :dispatch-n (list (ce/success-event action))})))

(re-frame/reg-event-fx
  ::execute-finish-activity
  (fn [{:keys [db]} [_ action]]
    (let [current-activity (get-in db [:progress-data :current-activity])
          current-lesson (get-in db [:progress-data :current-lesson])
          activity-lesson (get-in db [:activity-lesson])]
      (if (and (= current-activity (:id action)) #_(= current-lesson activity-lesson))
        {:dispatch-n (list [::next-workflow-action] (ce/success-event action))}
        {:dispatch-n (list (ce/success-event action))}))))

(re-frame/reg-event-fx
  ::next-workflow-action
  (fn [{:keys [db]} _]
    (let [workflow-actions (get-in db [:course-data :workflow-actions])
          finished-actions (get-in db [:progress-data :finished-workflow-actions])
          next-action (->> workflow-actions
                           (filter #(contains? finished-actions (:id %)))
                           (sort-by :order)
                           first)]
      (case (:type next-action)
        "set-activity" {:dispatch [::set-activity next-action]}))))

(re-frame/reg-event-fx
  ::set-activity
  (fn [{:keys [db]} [_ action]]
    {:db (-> db
             (update-in [:progress-data] assoc :current-activity (:activity action))
             (assoc-in [:progress-data :finished-workflow-actions (:id action)] true))
     :dispatch [:progress-data-changed]}))

(re-frame/reg-event-fx
  :progress-data-changed
  (fn [{:keys [db]} _]
    (let [course-id (:current-course db)
          progress (:progress-data db)]
      {:db (assoc-in db [:loading :save-progress] true)
       :http-xhrio {:method          :post
                    :uri             (str "/api/courses/" course-id "/current-progress")
                    :params          {:progress progress}
                    :format          (json-request-format)
                    :response-format (json-response-format {:keywords? true})
                    :on-success      [::save-progress-success]
                    :on-failure      [:api-request-error :save-progress]}})))

(re-frame/reg-event-fx
  ::save-progress-success
  (fn [_ _]
    {:dispatch-n (list [:complete-request :save-progress])}))

(re-frame/reg-event-fx
  ::set-music-volume
  (fn [{:keys [db]} [_ value]]
    {:db (assoc db :music-volume value)
     :music-volume value}))

(re-frame/reg-event-fx
  ::set-effects-volume
  (fn [{:keys [db]} [_ value]]
    {:db (assoc db :effects-volume value)
     :effects-volume value}))

(re-frame/reg-event-fx
  ::start-course
  (fn [{:keys [db]} [_ course-id]]
    (if (not= course-id (:current-course db))
      {:db (-> db
               (assoc :current-course course-id)
               (assoc :ui-screen :course-loading)
               (assoc-in [:loading :load-course] true))
       :load-course course-id})))

(re-frame/reg-event-fx
  ::set-current-scene
  (fn [{:keys [db]} [_ scene-id]]
    (let [loaded (get-in db [:scene-loading-complete scene-id])
          current-scene (:current-scene db)]
      (cond-> {:db (-> db
                       (assoc :current-scene scene-id)
                       (assoc :current-scene-data (get-in db [:scenes scene-id]))
                       (assoc :scene-started false))
               :dispatch-n (list [::vars.events/execute-clear-vars] [::ce/execute-remove-flows {:flow-tag (str "scene-" current-scene)}])}
              (not loaded) (assoc :load-scene [(:current-course db) scene-id])))))

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
      {:db (assoc db :progress-data default-progress)})))

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
        {:dispatch [::set-current-scene prev]}
        (set! (.-location js/window) "/")))))

(re-frame/reg-event-fx
  ::load-progress
  (fn [{:keys [db]} [_ course-id]]
    {:db (assoc-in db [:loading :load-progress] true)
     :load-progress course-id}))

(re-frame/reg-event-fx
  ::load-lessons
  (fn [{:keys [db]} [_ course-id]]
    {:db (-> db
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