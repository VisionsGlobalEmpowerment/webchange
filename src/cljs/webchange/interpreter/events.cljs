(ns webchange.interpreter.events
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.core :as i]
    [webchange.interpreter.executor :as e]
    [webchange.common.events :as ce]
    [webchange.interpreter.variables.events :as vars.events]
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
    (i/load-course course-id (fn [course] (do (re-frame/dispatch [::set-course-data course])
                                              (re-frame/dispatch [::set-current-scene (:initial-scene course)]))))))

(re-frame/reg-fx
  :load-scene
  (fn [[course-id scene-id]]
    (i/load-scene course-id scene-id (fn [scene] (re-frame/dispatch [::set-scene scene-id scene])))))

(re-frame/reg-fx
  :reload-asset
  (fn [asset]
    (i/load-asset asset)))

(re-frame/reg-fx
  :transition
  (fn [params]
    (i/interpolate params)))

(re-frame/reg-fx
  :execute-animation
  (fn [{:keys [state id] :as action}]
    (let [loop (if (contains? action :loop) (:loop action) true)]
      (.setAnimation state 0 id loop))))

(defn get-audio-key
  [db id]
  (get-in db [:scenes (:current-scene db) :audio (keyword id)]))

(ce/reg-simple-executor :audio ::execute-audio)
(ce/reg-simple-executor :state ::execute-state)
(ce/reg-simple-executor :add-alias ::execute-add-alias)
(ce/reg-simple-executor :empty ::execute-empty)
(ce/reg-simple-executor :animation ::execute-animation)
(ce/reg-simple-executor :scene ::execute-scene)
(ce/reg-simple-executor :transition ::execute-transition)
(ce/reg-simple-executor :placeholder-audio ::execute-placeholder-audio)

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
                          (assoc :on-ended (ce/dispatch-success-fn action)))})
    ))

(re-frame/reg-event-fx
  ::execute-transition
  (fn [{:keys [db]} [_ {:keys [transition-id to] :as action}]]
    (let [scene-id (:current-scene db)
          transition (get-in db [:transitions scene-id transition-id])]
      {:transition {:component transition
                    :to        to
                    :on-ended  (ce/dispatch-success-fn action)}})
    ))

(re-frame/reg-event-fx
  ::execute-scene
  (fn [{:keys [db]} [_ {:keys [scene-id] :as action}]]
    {:dispatch-n (list [::set-current-scene scene-id] (ce/success-event action))}))

(re-frame/reg-event-fx
  ::execute-audio
  [ce/event-as-action ce/with-flow]
  (fn [{:keys [db]} {:keys [id] :as action}]
    {:execute-audio (-> action
                        (assoc :key (get-audio-key db id))
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
      {:execute-animation (-> action
                              (assoc :state (get-in db [:scenes scene-id :animations (:target action)])))
       :dispatch-n (list (ce/success-event action))})))

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

(re-frame/reg-event-db
  ::register-transition
  (fn [db [_ name component]]
    (let [scene-id (:current-scene db)]
      (assoc-in db [:transitions scene-id name] component))))

(re-frame/reg-event-db
  ::register-animation
  (fn [db [_ name state]]
    (let [scene-id (:current-scene db)]
      (assoc-in db [:scenes scene-id :animations name] state))))


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
