(ns webchange.common.events
  (:require
    [clojure.string :as s]
    [re-frame.core :as re-frame]
    [day8.re-frame.tracing :refer-macros [fn-traced]]))

(def executors (atom {}))
(def on-skip-handlers
  "A list of functions to invoke on skip actions.
  Is used to cancel current action (e.g. stop audio, finish transition) and continue the flow (success event)."
  (atom []))

(defn on-skip!
  "Register function to invoke on skip action"
  [handler]
  (when handler
    (swap! on-skip-handlers conj handler)))

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

(defn register-flow-tags-event
  [{:keys [flow-id tags]}]
  [::register-flow-tags flow-id tags])

(defn get-action-tags
  [{:keys [tags unique-tag skippable]}]
  (cond-> tags
          unique-tag (conj unique-tag)
          skippable (conj "skip")))

(defn flow-registered?
  [flows tag]
  (some #(contains? (:tags %) tag) (vals flows)))

(def event-as-action
  "Interceptor
  Transform event arguments to action-data"
  (re-frame/->interceptor
    :before  (fn [context]
               (update-in context [:coeffects :event] #(second %)))))

(def with-flow
  "Interceptor
  Add and register flow if it is not defined"
  (re-frame/->interceptor
    :before  (fn [context]
               (let [action (get-in context [:coeffects :event])
                     db (get-in context [:coeffects :db])
                     registered? (flow-registered? (:flows db) (:unique-tag action))
                     flow-id (:flow-id action)]
                 (if (or flow-id registered?)
                   context
                   (-> context
                     (update-in [:coeffects :event] assoc :flow-id (random-uuid) :action-id (random-uuid) :register-flow true))
                   )))
    :after (fn [context]
             (let [action (get-in context [:coeffects :event])
                   register-flow (:register-flow action)
                   original-dispatch (get-in context [:effects :dispatch-n] (list))]
               (if register-flow
                 (-> context
                     (assoc-in [:effects :dispatch-n] (conj original-dispatch [::register-flow {:flow-id (:flow-id action)
                                                                                                :actions [(:action-id action)]
                                                                                                :type :all :tags (get-action-tags action)}])))
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
     (if-not (nil? action)
       (-> action
           (with-prev prev))
       (-> (str "Action '" id "' was not found") js/Error. throw)))))

(reg-executor :action (fn [{:keys [db action]}] [::execute-action (-> action
                                                                      :id
                                                                      (get-action db action)
                                                                      (assoc :flow-id (:flow-id action))
                                                                      (assoc :action-id (:action-id action)))]))
(reg-simple-executor :sequence ::execute-sequence)
(reg-simple-executor :sequence-data ::execute-sequence-data)
(reg-simple-executor :parallel ::execute-parallel)
(reg-simple-executor :remove-flows ::execute-remove-flows)
(reg-simple-executor :remove-flow-tag ::execute-remove-flow-tag)
(reg-simple-executor :callback ::execute-callback)
(reg-simple-executor :hide-skip ::execute-hide-skip)

(re-frame/reg-event-fx
  ::execute-action
  [event-as-action with-vars with-flow]
  (fn-traced [{:keys [db]} {:keys [type return-immediately unique-tag flow-id tags] :as action}]
    (if (flow-registered? (:flows db) unique-tag)
      {:dispatch [::discard-flow flow-id]}
      (let [handler (get @executors (keyword type))]
        {:dispatch-n (list (handler {:db db :action action})
                           (when return-immediately (success-event action))
                           (when tags (register-flow-tags-event action)))}))))

(re-frame/reg-event-fx
  ::discard-flow
  (fn [{:keys [db]} [_ flow-id]]
    (let [flow (get-in db [:flows flow-id])
          handler (:on-remove flow)]
      (when handler
        (handler))
      {:db (update db :flows dissoc flow-id)})))

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

(defn remove-tag
  [flow tag]
  (let [tags (->> (:tags flow)
                  (remove #{tag})
                  (into #{}))]
    (assoc flow :tags tags)))

(defn add-tags
  [flow tags]
  (let [tags (->> (:tags flow)
                  (concat tags)
                  (into #{}))]
    (assoc flow :tags tags)))

(defn destroy-timer
  [timer]
  (case (:type timer)
    "interval" (.clearInterval js/window (:id timer))
    (throw (js/Error. (str "Timer type '" (:type timer) "' is not supported")))))

(re-frame/reg-event-fx
  ::execute-register-timer
  (fn [{:keys [db]} [_ {:keys [name] :as timer}]]
    {:db (assoc-in db [:timers name] timer)}))

(re-frame/reg-event-fx
  ::execute-remove-timer
  (fn [{:keys [db]} [_ {:keys [name]}]]
    (let [timer (get-in db [:timers name])]
      (when-not (nil? timer)
        (destroy-timer timer))
      {:db (update db :timers dissoc name)})))

(re-frame/reg-event-fx
  ::execute-remove-timers
  (fn [{:keys [db]} [_]]
    (let [timers-to-remove (->> (get-in db [:timers])
                               (map second))]
      (doseq [timer timers-to-remove]
        (destroy-timer timer))
      {:db       (assoc db :timers {})})))

(re-frame/reg-event-fx
  ::register-flow
  (fn [{:keys [db]} [_ flow]]
    (let [scene-id (:current-scene db)
          flow-id (:flow-id flow)
          current-flow (get-in db [:flows flow-id])
          original-tags (into #{} (:tags current-flow))
          flow-data (-> current-flow
                        (merge flow)
                        (update-in [:tags] #(into original-tags %))
                        (update-in [:tags] conj (str "scene-" scene-id)))]
      {:db       (assoc-in db [:flows flow-id] flow-data)
       :dispatch [::check-flow flow-id]})))

(re-frame/reg-event-fx
  ::register-flow-remove-handler
  (fn [{:keys [db]} [_ {:keys [flow-id handler]}]]
    {:db (update-in db [:flows flow-id :on-remove] conj handler)}))

(re-frame/reg-event-fx
  ::register-flow-tags
  (fn [{:keys [db]} [_ flow-id tags]]
    (when flow-id
      {:db (update-in db [:flows flow-id] add-tags tags)})))

(defn flow-ancestors
  [db flow-id]
  (let [flow (get-in db [:flows flow-id])
        parent (:parent flow)]
    (if parent
      (concat [flow] (flow-ancestors db parent))
      [flow])))

(re-frame/reg-event-fx
  ::execute-remove-flow-tag
  (fn [{:keys [db]} [_ {:keys [flow-id tag] :as action}]]
    (let [
          flow-ancestors (->> (flow-ancestors db flow-id)
                         (map #(remove-tag % tag))
                         (map (juxt :flow-id identity))
                         (into {}))]
      {:db       (update db :flows merge flow-ancestors)
       :dispatch (success-event action)})))

(re-frame/reg-event-fx
  ::flow-success
  (fn [{:keys [db]} [_ flow-id action-id]]
    (when flow-id
      (let [succeeded (get-in db [:flows flow-id :succeeded] #{})]
        {:db (assoc-in db [:flows flow-id :succeeded] (conj succeeded action-id))
         :dispatch [::check-flow flow-id]}))))

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
  (fn-traced [{:keys [db]} action]
    (let [data (->> (:data action)
                    (map #(get-action % db action))
                    (into []))]
      {:dispatch [::execute-sequence-data (assoc action :data data :type "sequence-data")]})))

(re-frame/reg-event-fx
  ::execute-sequence-data
  [event-as-action with-vars]
  (fn-traced [{:keys [db]} action]
    (if (empty? (:data action))
      {:dispatch (success-event action)}
      (let [[current & rest] (:data action)
            sequence-skippable? (:skippable action)
            skippable? (:skippable current)
            rest (if skippable? (into [{:type "hide-skip"}] rest) rest)
            next [::execute-sequence-data (-> action (assoc :data rest))]
            flow-id (random-uuid)
            action-id (random-uuid)
            flow-event [::register-flow {:flow-id flow-id :actions [action-id] :type :all :next next :parent (:flow-id action) :tags (get-action-tags action)}]
            current-action (-> current
                               (assoc :flow-id flow-id)
                               (assoc :action-id action-id)
                               (with-prev action))]
        (when skippable?
          (on-skip! #(re-frame/dispatch (success-event current-action))))
        (when sequence-skippable?
          (on-skip! (:on-skip action))
          (on-skip! #(re-frame/dispatch [::execute-remove-flows {:flow-tag "skip"}])))
        {:dispatch-n (list flow-event
                           (when skippable? [::show-skip true])
                           [::execute-action current-action])}))))

(re-frame/reg-event-fx
  ::execute-parallel
  [event-as-action with-vars]
  (fn-traced [{:keys [db]} action]
    (let [flow-id (random-uuid)
          sequence-skippable? (:skippable action)
          actions (->> (:data action)
                      (map (fn [v] (assoc v :flow-id flow-id :action-id (random-uuid))))
                      (map (fn [v] (with-prev v action))))
          action-ids (map #(get % :action-id) actions)
          flow-event [::register-flow {:flow-id flow-id :actions action-ids :type :all :next (success-event action) :parent (:flow-id action) :tags (get-action-tags action)}]
          action-events (map (fn [a] [::execute-action a]) actions)]
      (when sequence-skippable?
        (on-skip! (:on-skip action))
        (on-skip! #(re-frame/dispatch [::execute-remove-flows {:flow-tag "skip"}])))
      {:dispatch-n (cons flow-event action-events)})))

(re-frame/reg-event-fx
  ::execute-callback
  [event-as-action with-flow]
  (fn [_ {:keys [callback] :as action}]
    (when-not (nil? callback)
      (callback))
    {:dispatch (success-event action)}))

(re-frame/reg-event-fx
  ::show-skip
  (fn [{:keys [db]} [_ value]]
    {:db (assoc db :show-skip value)}))

(re-frame/reg-event-fx
  ::execute-hide-skip
  [event-as-action with-flow]
  (fn [{:keys [db]} action]
    {:db (assoc db :show-skip false)
     :dispatch (success-event action)}))

(re-frame/reg-event-fx
  ::execute-reset-skip
  (fn [{:keys [db]} _]
    (reset! on-skip-handlers [])
    {:db (assoc db :show-skip false)}))

(re-frame/reg-event-fx
  ::skip
  (fn [{:keys [db]} _]
    (let [[on-skip-list _] (reset-vals! on-skip-handlers [])]
      (doseq [on-skip on-skip-list]
        (on-skip)))
    {:db (assoc db :show-skip false)}))
