(ns webchange.interpreter.events
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.core :as i]
    [webchange.interpreter.executor :as e]
    [webchange.interpreter.variables.events :as vars.events]
    ))

(re-frame/reg-fx
  :execute-audio
  (fn [params]
    (e/init)
    (e/execute-audio params)))

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
    (i/load-course course-id (fn [course] (re-frame/dispatch [::set-current-scene (:initial-scene course)])))))

(re-frame/reg-fx
  :load-scene
  (fn [[course-id scene-id]]
    (i/load-scene course-id scene-id (fn [scene] (re-frame/dispatch [::set-scene scene-id scene])))))

(re-frame/reg-fx
  :transition
  (fn [params]
    (i/interpolate params)))

(defn get-audio-key
  [db id]
  (get-in db [:scenes (:current-scene db) :audio (keyword id)]))

(defn get-action
  [id db]
  (get-in db [:scenes (:current-scene db) :actions (keyword id)]))

(re-frame/reg-event-fx
  ::execute-action
  (fn [{:keys [db]} [_ {:keys [type] :as action}]]
    (case (keyword type)
      :action {:dispatch [::execute-action (get-action (:id action) db)]}
      :audio {:dispatch [::execute-audio action]}
      :sequence {:dispatch [::execute-sequence action]}
      :parallel {:dispatch [::execute-parallel action]}
      :state {:dispatch [::execute-state action]}
      :empty {:dispatch [::execute-empty action]}
      :animation {:dispatch [::execute-animation action]}
      :scene {:dispatch [::execute-scene action]}
      :transition {:dispatch [::execute-transition action]}
      :dataset-var-provider {:dispatch [::vars.events/execute-dataset-var-provider action]}
      :vars-var-provider {:dispatch [::vars.events/execute-vars-var-provider action]}
      :placeholder-audio {:dispatch [::execute-placeholder-audio action]}
      :remove-flows {:dispatch [::execute-remove-flows action]}
      )))

(defn success-event
  [{:keys [flow-id action-id]}]
  [::flow-success flow-id action-id])

(defn dispatch-success-fn
  [action]
  #(re-frame/dispatch (success-event action)))

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
                          (assoc :on-ended (dispatch-success-fn action)))})
    ))

(re-frame/reg-event-fx
  ::execute-transition
  (fn [{:keys [db]} [_ {:keys [transition-id to] :as action}]]
    (let [scene-id (:current-scene db)
          transition (get-in db [:transitions scene-id transition-id])]
      {:transition {:component transition
                    :to        to
                    :on-ended  (dispatch-success-fn action)}})
    ))

(re-frame/reg-event-fx
  ::execute-scene
  (fn [{:keys [db]} [_ {:keys [scene-id] :as action}]]
    {:dispatch-n (list [::set-current-scene scene-id] (success-event action))}))

(re-frame/reg-event-fx
  ::execute-audio
  (fn [{:keys [db]} [_ {:keys [id] :as action}]]
      {:execute-audio (-> action
                          (assoc :key (get-audio-key db id))
                          (assoc :on-ended (dispatch-success-fn action)))}))

(def event-as-action
  (re-frame/->interceptor
    :before  (fn [context]
               (update-in context [:coeffects :event] #(second %)))))

(re-frame/reg-event-fx
  ::execute-sequence
  [event-as-action]
  (fn [{:keys [db]} action]
    (let [flow-id (random-uuid)
          action-id (random-uuid)
          [current & rest] (:data action)
          next [::execute-sequence (-> action (assoc :data rest))]
          flow-event [::register-flow {:flow-id flow-id :actions [action-id] :type :all :next next :tag (:tag action)}]]
      (if current
        {:dispatch-n (list flow-event
                           [::execute-action (-> current
                                                 (get-action db)
                                                 (assoc :flow-id flow-id)
                                                 (assoc :action-id action-id))])}
        {:dispatch (success-event action)}))))

(re-frame/reg-event-fx
  ::execute-state
  (fn [{:keys [db]} [_ {:keys [target id next] :as action}]]
    (let [scene-id (:current-scene db)
          scene (get-in db [:scenes scene-id])
          object (get-in scene [:objects (keyword target)])
          state (get-in object [:states (keyword id)])]
      {:db (update-in db [:scenes scene-id :objects (keyword target)] merge state)
       :dispatch-n (list (success-event action) next)})))

(re-frame/reg-event-fx
  ::execute-parallel
  (fn [{:keys [db]} [_ action]]
    (let [flow-id (random-uuid)
          actions (map (fn [v] (assoc v :flow-id flow-id :action-id (random-uuid))) (:data action))
          action-ids (map #(get % :action-id) actions)
          flow-event [::register-flow {:flow-id flow-id :actions action-ids :type :all :next (success-event action) :tag (:tag action)}]
          action-events (map (fn [a] [::execute-action a]) actions)]
      {:dispatch-n (cons flow-event action-events)})
    ))

(re-frame/reg-event-fx
  ::execute-empty
  (fn [{:keys [db]} [_ action]]
    {:dispatch-later [{:ms (:duration action) :dispatch (success-event action)}]}))

(re-frame/reg-event-fx
  ::execute-animation
  (fn [{:keys [db]} [_ action]]
    {:dispatch-n (list (success-event action))}))

(re-frame/reg-event-fx
  ::execute-remove-flows
  (fn [{:keys [db]} [_ {:keys [flow-tag] :as action}]]
    (let [flows (->> (get-in db [:flows])
                     (filter (fn [[k v]] (not= flow-tag (:tag v))))
                     (into {}))]
      {:db       (assoc db :flows flows)
       :dispatch (success-event action)})))

(re-frame/reg-event-fx
  ::register-flow
  (fn [{:keys [db]} [_ flow]]
    (let [flow-id (:flow-id flow)
          current-flow (get-in db [:flows flow-id])]
      {:db       (assoc-in db [:flows flow-id] (merge current-flow flow))
       :dispatch [::check-flow flow-id]})))

(re-frame/reg-event-fx
  ::flow-success
  (fn [{:keys [db]} [_ flow-id action-id]]
    (let [succeeded (get-in db [:flows flow-id :succeeded] #{})]
    {:db (assoc-in db [:flows flow-id :succeeded] (conj succeeded action-id))
     :dispatch [::check-flow flow-id]})))

(defn flow-finished?
  [{:keys [type actions succeeded] :as flow}]
  (case type
    :all (if (= (into #{} actions) succeeded) true false)
    :any (if (not-empty succeeded) true false)
    false))

(re-frame/reg-event-fx
  ::check-flow
  (fn [{:keys [db]} [_ flow-id]]
    (let [{:keys [next] :as flow} (get-in db [:flows flow-id])]
      (if (flow-finished? flow)
        (cond-> {}
                :always (assoc :db (update db :flows dissoc flow-id))
                next (assoc :dispatch next))))))

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
    {:db (assoc db :current-course course-id)
     :load-course course-id}))

(re-frame/reg-event-fx
  ::set-current-scene
  (fn [{:keys [db]} [_ scene-id]]
    (let [loaded (get-in db [:scene-loading-complete scene-id])]
      (cond-> {:db (-> db
                       (assoc :current-scene scene-id)
                       (assoc :scene-started false))}
              (not loaded) (assoc :load-scene [(:current-course db) scene-id])))))

(re-frame/reg-event-db
  ::set-scene
  (fn [db [_ scene-id scene]]
    (assoc-in db [:scenes scene-id] scene)))

(re-frame/reg-event-db
  ::register-transition
  (fn [db [_ name component]]
    (let [scene-id (:current-scene db)]
      (assoc-in db [:transitions scene-id name] component))))

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
                       (map (fn [action] [::execute-action action])))]
      {:dispatch-n actions})))