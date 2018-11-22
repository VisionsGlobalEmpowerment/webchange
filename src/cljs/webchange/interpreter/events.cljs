(ns webchange.interpreter.events
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.core :as i]
    [webchange.interpreter.executor :as e]
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
      )))

(re-frame/reg-event-fx
  ::execute-transition
  (fn [{:keys [db]} [_ {:keys [transition-id to next flow-id action-id] :as params}]]
    (let [scene-id (:current-scene db)
          transition (get-in db [:transitions scene-id transition-id])
          success-event [::flow-success flow-id action-id]]
      {:transition {:component transition
                    :to        to
                    :on-ended  #(doseq [event (list success-event next)]
                                  (when event (re-frame/dispatch event)))}})
    ))

(re-frame/reg-event-fx
  ::execute-scene
  (fn [{:keys [db]} [_ {:keys [scene-id flow-id action-id] :as params}]]
    {:dispatch-n (list [::set-current-scene scene-id] [::flow-success flow-id action-id])}))

(re-frame/reg-event-fx
  ::execute-audio
  (fn [{:keys [db]} [_ {:keys [id next flow-id action-id] :as params}]]
    (let [success-event [::flow-success flow-id action-id]]
      {:execute-audio (-> params
                          (assoc :key (get-audio-key db id))
                          (assoc :on-ended #(doseq [event (list success-event next)]
                                              (when event (re-frame/dispatch event)))))})))


(defn create-sequence-action
  [next]
  {:type "sequence"
   :data next})

(defn with-next
  [action next [_ {current-sequence :data}]]
  (if next
    (assoc action :next [::execute-sequence (create-sequence-action (concat next current-sequence))])
    action))

(re-frame/reg-event-fx
  ::execute-sequence
  (fn [{:keys [db]} [_ action]]
    (let [[current & next] (:data action)
          current-action (-> current
                             (get-action db)
                             (with-next next (:next action)))]
      (js/console.log "execute sequence")
      (js/console.log current-action)
      {:dispatch [::execute-action current-action]})))

(re-frame/reg-event-fx
  ::execute-state
  (fn [{:keys [db]} [_ {:keys [target id next flow-id action-id]}]]
    (let [scene-id (:current-scene db)
          scene (get-in db [:scenes scene-id])
          object (get-in scene [:objects (keyword target)])
          state (get-in object [:states (keyword id)])
          success [::flow-success flow-id action-id]]
      {:db (update-in db [:scenes scene-id :objects (keyword target)] merge state)
       :dispatch-n (list success next)})))

(re-frame/reg-event-fx
  ::execute-parallel
  (fn [{:keys [db]} [_ params]]
    (let [flow-id (random-uuid)
          actions (map (fn [v] (assoc v :flow-id flow-id :action-id (random-uuid))) (:data params))
          action-ids (into #{} (map #(get % :action-id) actions))
          flow-event [::register-flow {:flow-id flow-id :actions action-ids :type :all :next (:next params)}]
          action-events (map (fn [a] [::execute-action a]) actions)]
      {:dispatch-n (cons flow-event action-events)})
    ))

(re-frame/reg-event-fx
  ::execute-empty
  (fn [{:keys [db]} [_ params]]
    (let [next (:next params)]
      (if next
        {:dispatch-later [{:ms (:duration params) :dispatch next}]}
        {}))
    ))

(re-frame/reg-event-fx
  ::execute-animation
  (fn [{:keys [db]} [_ {:keys [flow-id action-id] :as params}]]
    {:dispatch-n (list [::flow-success flow-id action-id])}))

(re-frame/reg-event-fx
  ::register-flow
  (fn [{:keys [db]} [_ flow]]
    (let [flow-id (:flow-id flow)
          current-flow (get-in db [:flows flow-id])]
    {:db (assoc-in db [:flows flow-id] (merge current-flow flow))})))

(re-frame/reg-event-fx
  ::flow-success
  (fn [{:keys [db]} [_ flow-id action-id]]
    (let [succeeded (get-in db [:flows flow-id :succeeded] #{})]
    {:db (assoc-in db [:flows flow-id :succeeded] (conj succeeded action-id))
     :dispatch [::check-flow flow-id]})))

(defn flow-finished?
  [{:keys [type actions succeeded] :as flow}]
  (case type
    :all (if (= actions succeeded) true false)
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
    (js/console.log "register transition: " name)
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