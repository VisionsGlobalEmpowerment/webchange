(ns webchange.common.events
  (:require
    [clojure.string :as s]
    [re-frame.core :as re-frame]
    [webchange.interpreter.variables.core :refer [variables]]
    [webchange.interpreter.renderer.state.scene :as scene]
    [webchange.interpreter.renderer.state.overlays :as overlays]
    [webchange.interpreter.renderer.scene.components.wrapper-interface :as w]
    [webchange.interpreter.sound :as sound]
    [webchange.logger.index :as logger]
    [webchange.utils.scene-data :as utils]))

(def executors (atom {}))
(def flows (atom {}))
(def timers (atom {}))

(declare get-action)
(declare execute-action)

(defn reg-executor
  [id executor]
  (swap! executors assoc id executor))

(defn prepare-params
  [action params from-params]
  (reduce-kv (fn [m k v]
               (assoc m k (get params (keyword v)))) action from-params))

(defn prepare-action
  [{:keys [params from-params var from-var] :as action}]
  (-> action
      (prepare-params params from-params)
      (prepare-params var from-var)))

(defn reg-simple-executor
  [id event-name]
  (let [handler (fn [{:keys [action]}] (re-frame/dispatch [event-name (prepare-action action)]))]
    (reg-executor id handler)))

(reg-executor :action
              (fn [{:keys [db action]}]
                "Execute `action` action - call another action by its name.

                Action params:
                :id - callable action name.

                Example 1: simple action call
                {:type 'action',
                 :id   'init-audio-correct'}

                Example 2: call concepts action
                {:type     'action',
                 :from-var [{:var-name     'current-concept',
                             :var-property 'mari-word'}

                Example 3: call action with params
                {:type   'action',
                 :id     'set-current-tool',
                 :params {:tool 'felt-tip'}}

                Example 4: call action by name from variable
                {:type     'action'
                 :from-var [{:action-property 'id',
                             :var-name        'current-audio-correct',
                             :possible-values [:mari-audio-correct-1 :mari-audio-correct-2]}]}"
                (execute-action db (-> action
                                       :id
                                       (get-action db action)
                                       (assoc :display-name (:id action))
                                       (assoc :flow-id (:flow-id action))
                                       (assoc :action-id (:action-id action))))))

(reg-simple-executor :sequence ::execute-sequence)
(reg-simple-executor :sequence-data ::execute-sequence-data)
(reg-simple-executor :parallel ::execute-parallel)
(reg-simple-executor :parallel-by-tag ::execute-parallel-by-tag)
(reg-simple-executor :remove-flows ::execute-remove-flows)
(reg-simple-executor :finish-flows ::execute-finish-flows)
(reg-simple-executor :remove-flow-tag ::execute-remove-flow-tag)
(reg-simple-executor :callback ::execute-callback)
(reg-simple-executor :hide-skip ::execute-hide-skip)
(reg-simple-executor :workflow ::execute-workflow)
(reg-simple-executor :skip ::execute-skip)

(def on-action-finished-handlers
  "A list of function to invoke when action is finished.
  Is used to hide skip button after skippable action is finished"
  (atom {}))

(defn success-event
  [{:keys [flow-id action-id]}]
  [::flow-success flow-id action-id])

(declare flow-success!)

(defn dispatch-success-fn
  [{:keys [flow-id action-id]}]
  (flow-success! flow-id action-id))

(defn get-action-tags
  [{:keys [tags unique-tag skippable]}]
  (cond-> tags
          unique-tag (conj unique-tag)
          skippable (conj "skip")))

(defn flow-not-registered?
  [tag]
  (not-any? #(some #{tag} (:tags %)) (vals @flows)))

(def event-as-action
  "Interceptor
  Transform event arguments to action-data"
  (re-frame/->interceptor
    :before (fn [context]
              (update-in context [:coeffects :event] #(second %)))))

(declare register-flow!)

(defn ->with-flow
  [action]
  (let [flow-id (:flow-id action)]
    (if flow-id
      action
      (let [flow-id (random-uuid)
            action-id (random-uuid)]
        (register-flow! {:flow-id       flow-id
                         :actions       [action-id]
                         :current-scene (:current-scene action)
                         :type          :all
                         :tags          (get-action-tags action)})
        (assoc action :flow-id flow-id :action-id action-id)))))

(def with-flow
  "Interceptor
  Add and register flow if it is not defined"
  (re-frame/->interceptor
    :before (fn [context]
              (let [action (-> context
                               (get-in [:coeffects :event])
                               (assoc :current-scene (get-in context [:coeffects :db :current-scene]))
                               ->with-flow)]
                (assoc-in context [:coeffects :event] action)))))

(defn with-prev
  [action prev]
  (-> action
      (assoc :var (:var prev))
      (update-in [:params] merge (:params prev))
      (update :tags concat (get-action-tags prev))))

(defn apply-template
  [template value]
  (let [prepared-value (-> value
                           (str)
                           (clojure.string/replace #"[_~.<>{}()!№%:,;#$%^&*+='`]" ""))]
    (clojure.string/replace template "%" prepared-value)))

(defn from-template
  [template value]
  (if template
    (if (vector? value)
      (->> value
           (map #(apply-template template %))
           vec)
      (apply-template template value))
    value))

(defn with-var-property
  []
  (fn [action {:keys [var-name var-property var-key action-property template to-vector offset var-property-from-var]}]
    (let [var (get @variables var-name)
          value (cond->> var
                         var-property-from-var ((keyword (get @variables var-property-from-var)))
                         var-property ((keyword var-property))
                         var-key (hash-map (keyword var-key))
                         to-vector (conj [])
                         template (from-template template)
                         offset (+ offset))
          should-merge-to-root (and var-name (not action-property))]
      (if should-merge-to-root
        (merge (dissoc action :from-var) value)
        (assoc-in action (map keyword (s/split action-property #"\.")) value)))))

(defn with-var-properties
  [action]
  (if-let [from-var (:from-var action)]
    (reduce (with-var-property) action from-var)
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
    (let [var (get @variables var-name)
          object-name (from-template object-name-template var)
          object (->> (keyword object-name)
                      (scene/get-scene-object db))]

      (when (nil? object)
        (-> (str "Object with name <" object-name "> was not found") js/Error. throw))

      (let [object-data (w/get-data object)
            object-property-path (map keyword (clojure.string/split object-property "."))
            object-property-value (let [val (get-in object-data object-property-path)]
                                    (if offset (+ val offset) val))
            action-property-path (map keyword (clojure.string/split action-property "."))]

        (when (nil? object-property-value)
          (-> (str "Object <" object-name "> doesn't have property <" object-property-path ">") js/Error. throw))

        (assoc-in action action-property-path object-property-value)))))

(defn with-var-object-properties
  [action db]
  (if-let [from-var-object (:from-var-object action)]
    (reduce (with-var-object-property db) action from-var-object)
    action))

(defn ->with-vars
  [db action]
  (-> action
      (with-param-properties)
      (with-var-properties)
      (with-progress-properties db)
      (with-var-object-properties db)))

(def with-vars
  (re-frame/->interceptor
    :before (fn [context]
              (let [{:keys [db event]} (:coeffects context)
                    action (->with-vars db event)]
                (assoc-in context [:coeffects :event] action)))))

(defn- get-scene-data
  [db]
  (get-in db [:scenes (:current-scene db)]))

(defn get-action
  ([id db]
   (get-action id db {}))
  ([id db prev]
   (let [scene-data (get-scene-data db)
         action (get-in scene-data [:actions (keyword id)])]
     (if-not (nil? action)
       (-> action
           (assoc :display-name id)
           (with-prev prev))
       (-> (str "Action '" id "' was not found") js/Error. throw)))))

(defn get-actions-by-tag
  [db tag]
  (if-not (nil? tag)
    (-> (get-scene-data db)
        (utils/find-actions-by-tag tag))
    nil))

(defn cond-action-data
  [{:keys [flow-id action-id] :as action} handler]
  (cond-> handler
          flow-id (assoc :flow-id flow-id)
          action-id (assoc :action-id action-id)
          :always (with-prev action)))

(defn cond-action [db {:keys [display-name] :as action} handler-type]
  (let [handler (get action handler-type)
        action-data (if (string? handler)
                      (get-action handler db action)
                      (-> handler
                          (assoc :display-name [display-name handler-type])))]
    (cond-action-data action action-data)))

(declare discard-flow!)
(declare register-flow-tags!)

(defn- action->fold-name
  [{:keys [display-name]}]
  (if (sequential? display-name)
    (->> display-name
         (map #(if (keyword? %) (clojure.core/name %) %))
         (clojure.string/join " > ")
         (keyword))
    (keyword display-name)))

(defn- sequenced-action->display-name
  [action sequence-position]
  (flatten [(:display-name action) sequence-position]))

(defn execute-action
  [db {:keys [unique-tag] :as action}]
  (if (flow-not-registered? unique-tag)
    (let [action (as-> action a
                       (assoc a :current-scene (:current-scene db))
                       (->with-flow a)
                       (->with-vars db a))
          {:keys [type return-immediately flow-id action-id]} action
          handler (get @executors (keyword type))
          display-name (action->fold-name action)
          tags (get-action-tags action)]
      (if (some? handler)
        (logger/trace-folded ["execute action" display-name (str "(" type ")")] "action data:" action)
        (logger/error "Executor for action" display-name "is not defined" action))
      (when tags (register-flow-tags! flow-id tags))
      (handler {:db db :action action})
      (when return-immediately
        (dispatch-success-fn action)))
    (dispatch-success-fn action)))

(re-frame/reg-event-fx
  ::execute-action
  [event-as-action]
  (fn [{:keys [db]} action]
    (execute-action db action)
    {}))

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

(defn remove-timer
  [name]
  (let [timer (get @timers name)]
    (when-not (nil? timer)
      (destroy-timer timer))
    (swap! timers dissoc name)
    {}))

(re-frame/reg-event-fx
  ::execute-register-timer
  (fn [{:keys [db]} [_ {:keys [name] :as timer}]]
    (swap! timers assoc name timer)
    {}))

(re-frame/reg-event-fx
  ::execute-remove-timer
  (fn [{:keys [db]} [_ {:keys [name]}]]
    (remove-timer name)))

(defn remove-timers!
  []
  (let [timers-to-remove (->> @timers
                              (map second))]
    (doseq [timer timers-to-remove]
      (destroy-timer timer))
    (reset! timers {})))

(re-frame/reg-event-fx
  ::execute-remove-timers
  (fn [{:keys [db]} [_]]
    (remove-timers!)
    {}))

(defn- flow-finished?
  [{:keys [type actions succeeded]}]
  (case type
    :all (if (= (into #{} actions) succeeded) true false)
    :any (if (not-empty succeeded) true false)
    false))

(defn- finish-flow!
  [flow-id]
  (let [{:keys [next] :as flow} (get @flows flow-id)]
    (swap! flows dissoc flow-id)
    (when next
      (next flow))))

(defn- force-finish-flow!
  [flow-id]
  (let [flow (get @flows flow-id)
        children (filter #(= flow-id (:parent %)) (vals @flows))]
    (doseq [handler (:on-remove flow)]
      (handler))
    (finish-flow! flow-id)
    (doseq [child children]
      (force-finish-flow! (:flow-id child)))))

(defn- check-flow!
  [flow-id]
  (let [flow (get @flows flow-id)]
    (when (flow-finished? flow)
      (finish-flow! flow-id))))

(defn- register-flow!
  [{:keys [flow-id current-scene] :as flow}]
  (let [current-flow (get @flows flow-id)
        original-tags (into #{} (:tags current-flow))
        flow-data (-> current-flow
                      (merge flow)
                      (update-in [:tags] #(into original-tags %))
                      (update-in [:tags] conj (str "scene-" current-scene)))]
    (swap! flows assoc flow-id flow-data)))

(defn register-flow-remove-handler!
  [flow-id handler]
  (swap! flows update-in [flow-id :on-remove] conj handler))

(defn- register-flow-tags!
  [flow-id tags]
  (when flow-id
    (swap! flows update flow-id add-tags tags)))

(defn- flow-ancestors
  [flow-id]
  (let [flow (get @flows flow-id)
        parent (:parent flow)]
    (if parent
      (concat [flow] (flow-ancestors parent))
      [flow])))

(defn- execute-remove-flow-tag!
  [{:keys [flow-id tag] :as action}]
  (let [flow-ancestors (->> (flow-ancestors flow-id)
                            (map #(remove-tag % tag))
                            (map (juxt :flow-id identity))
                            (into {}))]
    (swap! flows merge flow-ancestors)
    (dispatch-success-fn action)))

(defn- flow-success!
  [flow-id action-id]
  (when flow-id
    (let [succeeded (get-in @flows [flow-id :succeeded] #{})]
      (swap! flows assoc-in [flow-id :succeeded] (conj succeeded action-id))
      (check-flow! flow-id)))
  (when action-id
    (let [on-action-finished (get @on-action-finished-handlers action-id)]
      (swap! on-action-finished-handlers dissoc action-id)
      (doseq [handler on-action-finished]
        (when handler
          (handler))))))

(defn skip-flow?
  "Check if action with given flow-id should be skipped.
  Will return true if any ancestor flow has ':skip' set to 'true'"
  [flow-id]
  (->> flow-id
       flow-ancestors
       (some :skip)))

(defn execute-remove-flows!
  [{:keys [flow-tag] :as action}]
  (let [flows-to-remove (->> @flows
                             (filter (fn [[k v]] (contains? (:tags v) flow-tag)))
                             (map second))
        flows-filtered (->> @flows
                            (filter (fn [[k v]] (not (contains? (:tags v) flow-tag))))
                            (into {}))]
    (doseq [flow flows-to-remove
            handler (:on-remove flow)]
      (handler))
    (reset! flows flows-filtered)
    (dispatch-success-fn action)))

(re-frame/reg-event-fx
  ::execute-remove-flows
  (fn [{:keys [db]} [_ action]]
    "Execute `remove-flows` action - terminate execution of actions marked with passed tag.
    Action supposed to be terminated should contain parameter `:tags`:
    {:type     'action',
     :tags     ['instruction']
     :from-var [{:var-name 'current-concept', :var-property 'game-voice-action'}]}

    Action params:
    :flow-tag - tag name.

    Example:
    {:type     'remove-flows',
     :flow-tag 'instruction'}"
    (execute-remove-flows! action)
    {}))

(re-frame/reg-event-fx
  ::execute-remove-flow-tag
  (fn [{:keys [db]} [_ action]]
    "Execute `remove-flow-tag` action - remove a tag from an action.
    Can be useful in case of `:unique-tag` parameter using. Only one action with specific `:unique-tag` can be run at the same time.

    Action params:
    :tag - tag name.

    Example:
    {:type 'remove-flow-tag',
     :tag  'clickable'}"
    (execute-remove-flow-tag! action)
    {}))

(re-frame/reg-event-fx
  ::flow-success
  (fn [{:keys [db]} [_ flow-id action-id]]
    (flow-success! flow-id action-id)
    {}))

(defn- execute-finish-flows!
  "Execute `finish-flows` action. 
   Removes flows with given tag, and continues flows stuck on removed flows.

   Action params:
   :tag - tag name.

   Example:
   {:type 'finish-flows',
    :tag  'question-1'}"
  [{:keys [tag] :as action}]
  (let [flows-to-remove (->> @flows
                             (filter (fn [[k v]] (contains? (:tags v) tag)))
                             (map first))]
    (doseq [flow-id flows-to-remove]
      (force-finish-flow! flow-id)))
  (dispatch-success-fn action))

(re-frame/reg-event-fx
  ::execute-finish-flows
  (fn [{:keys [db]} [_ action]]
    (execute-finish-flows! action)
    {}))

(re-frame/reg-event-fx
  ::execute-sequence
  [event-as-action with-vars]
  (fn [{:keys [db]} action]
    "Execute `sequence` action - run a sequence of actions defined by their names.

    Action params:
    :data - actions names vector.

    Example:
    {:type        'sequence',
     :data        ['start-activity' 'clear-instruction' 'reset-tools' 'init-current-tool']}"
    (let [data (->> (:data action)
                    (map #(-> %
                              (get-action db action)
                              (assoc :display-name (keyword %))))
                    (into []))]
      {:dispatch [::execute-sequence-data (assoc action :data data :type "sequence-data")]})))

(defn execute-sequence-data!
  ([db {:keys [flow-id] :as action}]
   (if (and (skip-flow? flow-id) (:workflow-user-input action))
     (dispatch-success-fn action)
     (execute-sequence-data! db action 0)))
  ([db action sequence-position]
   (if (empty? (remove nil? (:data action)))
     (when-not (:workflow-user-input action)
       (dispatch-success-fn action))
     (let [action (->with-vars db action)
           [current & rest] (:data action)
           next #(execute-sequence-data! db
                                         (-> action (assoc :data rest :previous-flow %))
                                         (inc sequence-position))
           flow-id (random-uuid)
           action-id (random-uuid)
           current-scene (:current-scene db)
           skippable? (->> action :previous-flow :tags (some #{"skip"}))
           flow-data {:flow-id flow-id
                      :actions [action-id]
                      :type :all
                      :next next
                      :parent (:flow-id action)
                      :skip (get-in action [:previous-flow :skip])
                      :tags (if skippable?
                              (conj (get-action-tags action) "skip")
                              (get-action-tags action))
                      :dialog (= "dialog" (:editor-type action))
                      :current-scene current-scene
                      :on-remove (when (:on-interrupt action)
                                   [#(execute-action db (:on-interrupt action))])}
           current-action (-> current
                              (update :display-name
                                      #(or % (sequenced-action->display-name action
                                                                             sequence-position)))
                              (assoc :flow-id flow-id)
                              (assoc :action-id action-id)
                              (with-prev action))]
       (register-flow! flow-data)
       (execute-action db current-action)))))

(re-frame/reg-event-fx
  ::execute-sequence-data
  [event-as-action]
  (fn [{:keys [db]} action]
    "Execute `sequence-data` action - run a sequence of actions defined by their data.

    Action params:
    :data - actions data vector.

    Example:
    {:type 'sequence-data',
     :data [{:type 'animation', :id 'volley_call', :target 'vera'}
            {:type 'add-animation', :id 'volley_idle', :target 'vera', :loop true}]}"
    (execute-sequence-data! db action)
    {}))

(defn execute-parallel!
  [db action]
  (let [action (->with-vars db action)
        flow-id (random-uuid)
        actions (->> (:data action)
                     (map (fn [v] (assoc v :flow-id flow-id :action-id (random-uuid))))
                     (map (fn [v] (with-prev v action))))
        action-ids (map #(get % :action-id) actions)
        current-scene (:current-scene db)
        flow-data {:flow-id       flow-id :actions action-ids :type :all
                   :parent        (:flow-id action) :tags (get-action-tags action)
                   :next          #(dispatch-success-fn action)
                   :current-scene current-scene}]
    (if (seq actions)
      (do
        (register-flow! flow-data)
        (doall (map-indexed (fn [sequence-position child-action]
                              (->> (sequenced-action->display-name action sequence-position)
                                   (assoc child-action :display-name)
                                   (execute-action db)))
                            actions)))
      (dispatch-success-fn action))))

(re-frame/reg-event-fx
  ::execute-parallel
  [event-as-action]
  (fn [{:keys [db]} action]
    "Execute `parallel` action - run in parallel several actions defined by their data.

    Action params:
    :data - actions data vector.

    Example:
    {:type 'parallel',
     :data [{:type 'state', :id 'hidden', :target 'letter-trace'}
            {:type 'state', :id 'hidden', :target 'letter-tutorial-path'}]}"
    (execute-parallel! db action)
    {}))

(defn execute-parallel-by-tag!
  [db action]
  (let [action (->with-vars db action)
        actions (->> (:tag action)
                     (get-actions-by-tag db)
                     (map (fn [[action-name]]
                            {:type "action"
                             :id   action-name})))
        action-to-execute (-> action
                              (assoc :type "parallel")
                              (assoc :data actions))]
    (if (seq actions)
      (execute-action db action-to-execute)
      (dispatch-success-fn action))))

(re-frame/reg-event-fx
  ::execute-parallel-by-tag
  [event-as-action]
  (fn [{:keys [db]} action]
    (execute-parallel-by-tag! db action)
    {}))

(defn execute-callback!
  [db {:keys [callback] :as action}]
  (let [action (-> action
                   (assoc :current-scene (:current-scene db))
                   (->with-flow))]
    (when-not (nil? callback)
      (callback))
    (dispatch-success-fn action)))

(re-frame/reg-event-fx
  ::execute-callback
  [event-as-action]
  (fn [{:keys [db]} action]
    "Execute `callback` action - call external function.
    This action is used when it is generated from cljs code.

    Action params:
    :callback - a function to be called.

    Example:
    {:type     'callback'
     :callback [object Function]}"
    (execute-callback! db action)
    {}))

(defn- enable-skip-in-flows!
  "Skip actions in skippable dialogs"
  []
  (swap! flows #(->> %
                     (map (fn [[k v]] (if (some #{"skip"} (:tags v))
                                        [k (assoc v :skip true)]
                                        [k v])))
                     (into {}))))

(defn skip
  "Immediately completes current skippable flow.
   Should set all variables and complete transitions.

   Example:
   {:type 'skip'}"
  []
  (enable-skip-in-flows!)
  (execute-finish-flows! {:tag "skip"})
  (re-frame/dispatch [::overlays/hide-skip-menu]))

(re-frame/reg-event-fx
  ::execute-skip
  [event-as-action with-flow]
  (fn [{:keys [db]} action]
    (skip)
    (dispatch-success-fn action)))

(defn- first-dialog-ancestor
  [flow-id]
  (let [flows-data @flows]
    (->> flow-id
         flow-ancestors
         (filter :dialog)
         first)))

(defn- execute-start-skip-region
  "Defines the starting point in dialogue where Skip is available
   Example:
   {:type 'start-skip-region'}"
  [{{:keys [flow-id] :as action} :action}]
  (let [dialog-flow-id (-> flow-id first-dialog-ancestor :flow-id)]
    (swap! flows update-in [dialog-flow-id :tags] conj "skip"))
  (re-frame/dispatch [::overlays/show-skip-menu])
  (dispatch-success-fn action))

(reg-executor :start-skip-region execute-start-skip-region)

(defn- execute-end-skip-region
  "Defines the end point in dialogue where Skip is available
   Example:
   {:type 'end-skip-region'}"
  [{{:keys [flow-id] :as action} :action}]
  (let [dialog-flow-id (-> flow-id first-dialog-ancestor :flow-id)
        remove-skip (fn [flow] (-> flow
                                   (remove-tag "skip")
                                   (assoc :skip false)))]
    (swap! flows update dialog-flow-id remove-skip))
  (re-frame/dispatch [::overlays/hide-skip-menu])
  (dispatch-success-fn action))

(reg-executor :end-skip-region execute-end-skip-region)

(defn- execute-mute-background-music
  "Defines the end point in dialogue where background music is muted
   Example:
   {:type 'mute-background-music'}"
  [{{:keys [_] :as action} :action}]
  (sound/music-mute)
  (dispatch-success-fn action))

(reg-executor :mute-background-music execute-mute-background-music)

(defn- execute-unmute-background-music
  "Defines the end point in dialogue where background music is unmuted
   Example:
   {:type 'unmute-background-music'}"
  [{{:keys [_] :as action} :action}]
  (sound/music-unmute)
  (dispatch-success-fn action))

(reg-executor :unmute-background-music execute-unmute-background-music)

(defn- init-workflow-indexes
  [{:keys [data] :as action}]
  (let [with-indexes (->> data
                          (map-indexed (fn [idx action] (assoc action :index idx)))
                          (into []))]
    (assoc action :data with-indexes)))

(defn- workflow-key
  [{key :key}]
  (or key "default-workflow-key"))

(defn- drop-completed-actions
  [{:keys [data] :as action}]
  (let [state (get @variables (workflow-key action) #{})
        filtered (->> data
                      (remove #(contains? state (:index %)))
                      (into []))]
    (assoc action :data filtered)))

(defn- complete-workflow-action!
  [workflow {idx :index}]
  (let [workflow-key (workflow-key workflow)
        state (get @variables workflow-key #{})]
    (when idx
      (swap! variables assoc workflow-key (conj state idx)))))

(defn- with-on-end
  [{on-end :on-end :as action}]
  (if on-end
    (update action :data conj {:type "action" :id on-end})
    action))

(defn- init-workflow
  [action]
  (if (:initialized action)
    action
    (-> action
        (with-on-end)
        (init-workflow-indexes)
        (drop-completed-actions)
        (assoc :initialized true))))

(defn execute-workflow!
  [db action]
  (if (empty? (:data action))
    (dispatch-success-fn action)
    (let [action (->> action
                      (->with-vars db)
                      (init-workflow))
          [current & rest] (:data action)
          rest (if (:workflow-user-input current) [] rest)
          next #(execute-workflow! db (-> action (assoc :data rest)))
          flow-id (random-uuid)
          action-id (random-uuid)
          flow-data {:flow-id       flow-id :actions [action-id] :type :all :next next
                     :parent        (:flow-id action) :tags (get-action-tags action)
                     :current-scene (:current-scene db)}
          current-action (-> current
                             (assoc :flow-id flow-id)
                             (assoc :action-id action-id)
                             (with-prev action))]
      (register-flow! flow-data)

      (complete-workflow-action! action current-action)
      (execute-action db current-action))))

(re-frame/reg-event-fx
  ::execute-workflow
  [event-as-action]
  (fn [{:keys [db]} action]
    "Execute `workflow` action - run a sequence of actions defined by their data.
    Each finished action is marked as completed. Next time this workflow sequence is executed all
    completed actions will be skipped. This allows executing workflow several times.

    Action params:
    :data - actions data vector.

    Example:
    {:type 'workflow',
     :data [{:type 'action', :id 'dialog'}
            {:type 'action', :id 'question', :workflow-user-input true}]}"
    (execute-workflow! db action)
    {}))
