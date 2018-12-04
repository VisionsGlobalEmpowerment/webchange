(ns webchange.common.events
  (:require
    [re-frame.core :as re-frame]))

(def executors (atom {}))

(defn prepare-params
  [action params from-params]
  (reduce-kv (fn [m k v] (assoc m k (get params (keyword v)))) action from-params))

(defn prepare-action
  [{:keys [params from-params var from-var] :as action}]
  (-> action
      (prepare-params params from-params)
      (prepare-params var from-var)))

(defn reg-executor
  [id executor]
  (swap! executors assoc id executor))

(defn reg-simple-executor
  [id event-name]
  (let [handler (fn [{:keys [action]}] [event-name (prepare-action action)])]
    (reg-executor id handler)))

(defn success-event
  [{:keys [flow-id action-id]}]
  [::flow-success flow-id action-id])

(defn dispatch-success-fn
  [action]
  #(re-frame/dispatch (success-event action)))

(def event-as-action
  (re-frame/->interceptor
    :before  (fn [context]
               (update-in context [:coeffects :event] #(second %)))))

(def with-flow
  (re-frame/->interceptor
    :before  (fn [context]
               (let [flow-id (get-in context [:coeffects :event :flow-id])]
                 (if flow-id
                   context
                   (-> context
                     (update-in [:coeffects :event] assoc :flow-id (random-uuid) :action-id (random-uuid) :register-flow true))
                   )))
    :after (fn [context]
             (let [action (get-in context [:coeffects :event])
                   register-flow (:register-flow action)]
               (if register-flow
                 (-> context
                     (assoc-in [:effects :dispatch-n] (list [::register-flow {:flow-id (:flow-id action)}])))
                 context))
             )))


(defn with-prev
  [action prev]
  (-> action
      (assoc :var (:var prev))
      (update-in [:params] merge (:params prev))))

(defn get-action
  ([id db]
    (get-action id db {}))
  ([id db prev]
   (let [action (get-in db [:scenes (:current-scene db) :actions (keyword id)])]
     (with-prev action prev))))


(reg-executor :action (fn [{:keys [db action]}] [::execute-action (-> action
                                                                      :id
                                                                      (get-action db action))]))
(reg-simple-executor :sequence ::execute-sequence)
(reg-simple-executor :parallel ::execute-parallel)
(reg-simple-executor :remove-flows ::execute-remove-flows)

(re-frame/reg-event-fx
  ::execute-action
  (fn [{:keys [db]} [_ {:keys [type] :as action}]]
    (let [handler (get @executors (keyword type))]
      {:dispatch (handler {:db db :action action})})))

(re-frame/reg-event-fx
  ::execute-remove-flows
  (fn [{:keys [db]} [_ {:keys [flow-tag] :as action}]]
    (let [flows-to-remove (->> (get-in db [:flows])
                               (filter (fn [[k v]] (contains? (:tags v) flow-tag)))
                               (map second))
          flows (->> (get-in db [:flows])
                     (filter (fn [[k v]] (not (contains? (:tags v) flow-tag))))
                     (into {}))]
      (doseq [flow flows-to-remove
              handler (:on-remove flow)]
        (handler))
      {:db       (assoc db :flows flows)
       :dispatch (success-event action)})))

(re-frame/reg-event-fx
  ::register-flow
  (fn [{:keys [db]} [_ flow]]
    (let [scene-id (:current-scene db)
          flow-id (:flow-id flow)
          current-flow (get-in db [:flows flow-id])
          flow-data (-> current-flow
                        (merge flow)
                        (update-in [:tags] #(into #{} %))
                        (update-in [:tags] conj (str "scene-" scene-id)))]
      {:db       (assoc-in db [:flows flow-id] flow-data)
       :dispatch [::check-flow flow-id]})))

(re-frame/reg-event-fx
  ::register-flow-remove-handler
  (fn [{:keys [db]} [_ {:keys [flow-id handler]}]]
    {:db (update-in db [:flows flow-id :on-remove] conj handler)}))

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
  ::execute-sequence
  [event-as-action]
  (fn [{:keys [db]} action]
    (let [flow-id (random-uuid)
          action-id (random-uuid)
          [current & rest] (:data action)
          next [::execute-sequence (-> action (assoc :data rest))]
          flow-event [::register-flow {:flow-id flow-id :actions [action-id] :type :all :next next :tags (:tags action)}]]
      (if current
        {:dispatch-n (list flow-event
                           [::execute-action (-> current
                                                    (get-action db action)
                                                    (assoc :flow-id flow-id)
                                                    (assoc :action-id action-id))])}
        {:dispatch (success-event action)}))))

(re-frame/reg-event-fx
  ::execute-parallel
  (fn [{:keys [db]} [_ action]]
    (let [flow-id (random-uuid)
          actions (->> (:data action)
                      (map (fn [v] (assoc v :flow-id flow-id :action-id (random-uuid))))
                      (map (fn [v] (with-prev v action))))
          action-ids (map #(get % :action-id) actions)
          flow-event [::register-flow {:flow-id flow-id :actions action-ids :type :all :next (success-event action) :tags (:tags action)}]
          action-events (map (fn [a] [::execute-action a]) actions)]
      {:dispatch-n (cons flow-event action-events)})))