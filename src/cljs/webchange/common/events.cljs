(ns webchange.common.events
  (:require
    [clojure.string :as s]
    [re-frame.core :as re-frame]
    [day8.re-frame.tracing :refer-macros [fn-traced]]))

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
                     (assoc-in [:effects :dispatch-n] (list [::register-flow {:flow-id (:flow-id action) :tags (:tags action)}])))
                 context))
             )))

(defn with-prev
  [action prev]
  (-> action
      (assoc :var (:var prev))
      (update-in [:params] merge (:params prev))))

(defn from-template
  [template value]
  (if template
    (clojure.string/replace template "%" value)
    value))

(defn with-var-property
  [db]
  (fn [action {:keys [var-name var-property var-key action-property template to-vector]}]
    (let [var (get-in db [:scenes (:current-scene db) :variables var-name])
          value (cond->> var
                         var-property ((keyword var-property))
                         var-key (hash-map (keyword var-key))
                         to-vector (conj [])
                         template (from-template template))
          should-merge-to-root (and var-name (not action-property))]
      (if should-merge-to-root
        (merge (dissoc action :from-var) value)
        (assoc-in action (map keyword (s/split action-property #"\.")) value)))))

(defn with-var-properties
  [action db]
  (if-let [from-var (:from-var action)]
    (reduce (with-var-property db) action from-var)
    action))

(defn with-param-property
  [action {:keys [param-property action-property template]}]
    (let [value (get-in action [:params (keyword param-property)])]
      (assoc action (keyword action-property) (from-template template value))))

(defn with-param-properties
  [action]
  (if-let [from-params (:from-params action)]
    (reduce with-param-property action from-params)
    action))

(defn with-progress-property
  [db]
  (fn [action {:keys [progress-property action-property template]}]
    (let [value (get-in db [:progress-data :variables (keyword progress-property)])]
      (assoc action (keyword action-property) (from-template template value)))))

(defn with-progress-properties
  [action db]
  (if-let [from-progress (:from-progress action)]
    (reduce (with-progress-property db) action from-progress)
    action))

(defn with-var-object-property
  [db]
  (fn [action {:keys [var-name object-name-template object-property action-property offset]}]
    (let [var (get-in db [:scenes (:current-scene db) :variables var-name])
          object (get-in db [:scenes (:current-scene db) :objects (keyword (from-template object-name-template var))])
          object-property-path (map keyword (clojure.string/split object-property "."))
          object-property-value (let [val (get-in object object-property-path)]
                                  (if offset (+ val offset) val))
          action-property-path (map keyword (clojure.string/split action-property "."))]
      (assoc-in action action-property-path object-property-value))))

(defn with-var-object-properties
  [action db]
  (if-let [from-var-object (:from-var-object action)]
    (reduce (with-var-object-property db) action from-var-object)
    action))

(def with-vars
  (re-frame/->interceptor
    :before  (fn [context]
               (let [{:keys [db event]} (:coeffects context)
                     action (-> event
                                (with-param-properties)
                                (with-var-properties db)
                                (with-progress-properties db)
                                (with-var-object-properties db))]
                 (assoc-in context [:coeffects :event] action)))))

(defn get-action
  ([id db]
    (get-action id db {}))
  ([id db prev]
   (let [action (get-in db [:scenes (:current-scene db) :actions (keyword id)])]
     (-> action
         (with-prev prev)))))

(defn flow-registered?
  [flows tag]
  (some #(contains? (:tags %) tag) (vals flows)))

(defn can-execute?
  [db {:keys [description unique-tag]}]
  (let [flow-registered? (flow-registered? (:flows db) unique-tag)]
    (not flow-registered?)))

(defn get-action-tags
  [{:keys [tags unique-tag]}]
  (if-not (nil? unique-tag)
    (conj tags unique-tag)
    tags))

(reg-executor :action (fn [{:keys [db action]}] [::execute-action (-> action
                                                                      :id
                                                                      (get-action db action)
                                                                      (assoc :flow-id (:flow-id action))
                                                                      (assoc :action-id (:action-id action)))]))
(reg-simple-executor :sequence ::execute-sequence)
(reg-simple-executor :sequence-data ::execute-sequence-data)
(reg-simple-executor :parallel ::execute-parallel)
(reg-simple-executor :remove-flows ::execute-remove-flows)

(re-frame/reg-event-fx
  ::execute-action
  [event-as-action with-vars]
  (fn-traced [{:keys [db]} {:keys [type return-immediately] :as action}]
    (if (can-execute? db action)
      (let [handler (get @executors (keyword type))]
        (if return-immediately
          {:dispatch-n (list (handler {:db db :action action})
                             (success-event action))}
          {:dispatch (handler {:db db :action action})})))))

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
  [event-as-action with-vars]
  (fn [{:keys [db]} action]
    (let [flow-id (random-uuid)
          action-id (random-uuid)
          [current & rest] (:data action)
          next [::execute-sequence (-> action (assoc :data rest))]
          flow-event [::register-flow {:flow-id flow-id :actions [action-id] :type :all :next next :tags (get-action-tags action)}]]
      (if current
        {:dispatch-n (list flow-event
                           [::execute-action (-> current
                                                 (get-action db action)
                                                 (assoc :flow-id flow-id)
                                                 (assoc :action-id action-id)
                                                 (with-prev action))])}
        {:dispatch (success-event action)}))))

(re-frame/reg-event-fx
  ::execute-sequence-data
  [event-as-action with-vars]
  (fn [{:keys [db]} action]
    (let [flow-id (random-uuid)
          action-id (random-uuid)
          [current & rest] (:data action)
          next [::execute-sequence-data (-> action (assoc :data rest))]
          flow-event [::register-flow {:flow-id flow-id :actions [action-id] :type :all :next next :tags (get-action-tags action)}]]
      (if current
        {:dispatch-n (list flow-event
                           [::execute-action (-> current
                                                 (assoc :flow-id flow-id)
                                                 (assoc :action-id action-id)
                                                 (with-prev action))])}
        {:dispatch (success-event action)}))))

(re-frame/reg-event-fx
  ::execute-parallel
  [event-as-action with-vars]
  (fn [{:keys [db]} action]
    (let [flow-id (random-uuid)
          actions (->> (:data action)
                      (map (fn [v] (assoc v :flow-id flow-id :action-id (random-uuid))))
                      (map (fn [v] (with-prev v action))))
          action-ids (map #(get % :action-id) actions)
          flow-event [::register-flow {:flow-id flow-id :actions action-ids :type :all :next (success-event action) :tags (get-action-tags action)}]
          action-events (map (fn [a] [::execute-action a]) actions)]
      {:dispatch-n (cons flow-event action-events)})))