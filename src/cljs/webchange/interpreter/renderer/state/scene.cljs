(ns webchange.interpreter.renderer.state.scene
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.renderer.state.db :refer [path-to-db]]
    [webchange.interpreter.renderer.scene.components.wrapper-interface :as w]
    [webchange.logger.index :as logger]))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [db]} [_ {:keys [views]}]]
    (let [objects (get-in db (path-to-db [:objects]) (atom {}))
          groups (get-in db (path-to-db [:groups]) (atom {}))]
      (reset! objects {})
      (reset! groups {})
      {:db (-> db
               (assoc-in (path-to-db [:objects]) objects)
               (assoc-in (path-to-db [:groups]) groups)
               (assoc-in (path-to-db [:views]) views))})))

(re-frame/reg-event-fx
  ::set-rendering-state
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db (path-to-db [:rendering?]) value)}))

(re-frame/reg-event-fx
  ::set-scene-layered-objects
  (fn [{:keys [db]} [_ object-names]]
    {:db (assoc-in db (path-to-db [:scene-layered-objects]) object-names)}))

(re-frame/reg-fx
 :add-scene-object
 (fn [{:keys [objects groups object-wrapper]}]
   (let [object-name (:name object-wrapper)
         group-name (:group-name object-wrapper)]
     (when (contains? @objects object-name)
       (-> (str "Object with name " object-name " already exists.") js/Error. throw))
     (swap! objects assoc object-name object-wrapper)
     (when-not (nil? group-name)
       (swap! groups update group-name conj object-name)))))

(re-frame/reg-fx
  :remove-scene-object
  (fn [{:keys [objects object-name]}]
    (swap! objects dissoc object-name)))

(re-frame/reg-event-fx
  ::register-object
  (fn [{:keys [db]} [_ object-wrapper]]
    {:add-scene-object {:objects        (get-in db (path-to-db [:objects]))
                        :groups         (get-in db (path-to-db [:groups]))
                        :object-wrapper object-wrapper}}))

(re-frame/reg-event-fx
  ::unregister-object
  (fn [{:keys [db]} [_ object-name]]
    {:remove-scene-object {:objects     (get-in db (path-to-db [:objects]))
                           :object-name object-name}}))

(defn get-scene-object
  [db name]
  (if-not (nil? name)
    (let [objects @(get-in db (path-to-db [:objects]))]
      (get objects name))
    nil))

(defn get-scene-group
  [db group-name]
  (let [groups @(get-in db (path-to-db [:groups]))
        group (get groups group-name)]
    (when-not (nil? group)
      (map #(get-scene-object db %) group))))

;; Change object events

(defn- filter-extra-props
  [props extra-props-names]
  (->> props
       (filter (fn [[prop-name]] (not (some #{prop-name} extra-props-names))))
       (into {})))

; Actions matching the template:
; 'handler-name = "set-" + param-name'
; (e.g. ':set-text' wrapper method for ':text' param)
; should be handled by 'generic-handler'
; Here should be defined only actions with different params
; (e.g. ':set-filter' wrapper method for ':brightness' param)
(def available-actions
  [{:action :set-position :params [:x :y]}
   {:action :set-scale :params [:scale :scale-x :scale-y]}
   {:action :set-visibility :params [:visible]}
   {:action :set-src :params [:src]}
   {:action :reset-video :params [:src]}
   {:action :clear-area :params []}
   {:action :set-filter :params [:filter :brightness :eager]}
   {:action :set-data :params [:data]}
   {:action :set-children :params [:children]}
   {:action :set-skeleton :params [:name] :accompany-params [:skin :skin-names]}
   {:action :set-combined-skin :params [:skin-names]}
   {:action :set-enable :params [:enable?]}
   {:action :set-chunks :params [:chunks]}
   {:action :set-anim :params [:anim]}])

(defn- get-action-params
  ":params - used to get action params and determine if action is needed for current params set
   :accompany-params - also needed for action but NOT used to determine if action is needed.
   For example apply params 'name', 'skin' and 'skin-names' to 'set-skeleton' action only if 'name' is defined.
   If 'skin' is defined but 'name' is undefined, use 'set-animation-skin' action instead."
  [{:keys [params accompany-params]
    :or   {params           []
           accompany-params []}}
   overall-params]
  (let [action-params (select-keys overall-params params)
        action-used? (seq action-params)]
    (if action-used?
      {:action-used?  action-used?
       :action-params (merge action-params (select-keys overall-params accompany-params))
       :rest-params   (apply dissoc overall-params (concat params accompany-params))}
      {:action-used?  action-used?
       :action-params {}
       :rest-params   overall-params})))

(declare change-scene-object)

(defn set-scene-object-state
  [db object-name state]
  (let [filtered-state (filter-extra-props state [:revert :start :target :volume])
        [execute-actions not-handled-params] (loop [actions-to-execute []
                                                    [{:keys [action to-generic?] :as available-action} & rest-available-actions] available-actions
                                                    current-state filtered-state]
                                               (when to-generic? ;; Remove check when no :to-generic? field in available-actions
                                                 (logger/warn (str "Change set param handler (" action ") to generic.")))
                                               (if (nil? available-action)
                                                 [actions-to-execute current-state]
                                                 (let [{:keys [action-used? action-params rest-params]} (get-action-params available-action current-state)]
                                                   (recur (if action-used?
                                                            (conj actions-to-execute [action action-params])
                                                            actions-to-execute)
                                                          rest-available-actions
                                                          rest-params))))
        generic-handlers (map (fn [[param-name param-value]]
                                (let [executor-name (->> param-name clojure.core/name (str "set-") keyword)]
                                  [executor-name param-value]))
                              not-handled-params)

        result-actions (cond-> execute-actions
                               (seq generic-handlers) (conj [:generic-handler generic-handlers]))]
    (change-scene-object {:db db} [nil object-name result-actions])))

(defn- event-handler
  [handler {:keys [db]} [_ & params]]
  (apply handler (concat [db] params)))

(re-frame/reg-event-fx
  ::set-scene-object-state
  (partial event-handler set-scene-object-state))

(defn get-object-name
  [db object-name]
  (or (get-scene-object db object-name)
      (get-scene-group db object-name)))

(defn- apply-to-wrapper
  [method target & params]
  (let [targets (if (sequential? target) target [target])]
    (doseq [wrapper targets]
      (apply method (concat [wrapper] params)))))

(defn- set-filter
  [[object-wrapper {:keys [filter] :as params}]]
  (apply-to-wrapper w/set-filter object-wrapper filter params))

(defn- set-position
  [[object-wrapper position]]
  (apply-to-wrapper w/set-position object-wrapper position))

(defn- set-scale
  [[object-wrapper scale]]
  (apply-to-wrapper w/set-scale object-wrapper (or (:scale scale) scale)))

(defn- clear-area
  [[object-wrapper {:keys [text]}]]
  (apply-to-wrapper w/clear-area object-wrapper text))

(defn- set-src
  "Options are used to reset video src"
  [[object-wrapper {:keys [src options]}]]
  (apply-to-wrapper w/set-src object-wrapper src options))

(defn- reset-video
  [[object-wrapper {:keys [src options]}]]
  (apply-to-wrapper w/reset-video object-wrapper src options))

(defn- set-children
  [[object-wrapper {:keys [children options]} db]]
  (let [object (:object object-wrapper)
        parent (.-parent (.-parent object))
        children (vec (array-seq (.-children object)))]
    (doseq [child children]
      (.addChild parent child))

    (doseq [child children]
      (.removeChild object child)))

  (doseq [child children]
    (apply-to-wrapper w/set-parent (get-object-name db (keyword child)) object-wrapper options)))

(defn- stop
  [[object-wrapper]]
  (apply-to-wrapper w/stop object-wrapper))

(defn- set-data
  [[object-wrapper params]]
  (apply-to-wrapper w/set-data object-wrapper params))

(re-frame/reg-event-fx
  ::set-traffic-light
  (fn [{:keys [db]} [_ object-name color]]
    (let [wrapper (get-scene-object db object-name)]
      (apply-to-wrapper w/set-traffic-light wrapper color))))

(defn- set-skeleton
  [[object-wrapper params]]
  (w/execute-method object-wrapper :set-skeleton params))

(defn- set-combined-skin
  [[object-wrapper {:keys [skin-names]}]]
  (w/execute-method object-wrapper :set-combined-skin skin-names))

(defn- set-enable
  [[object-wrapper {:keys [enable?]}]]
  (w/execute-method object-wrapper :set-enable enable?))

(defn- set-chunks
  [[{:keys [update-chunks]} params]]
  (update-chunks {:params params}))

(defn set-visibility
  [[object-wrapper {:keys [visible]}]]
  (w/execute-method object-wrapper :set-visibility visible))

(defn set-anim
  [[object-wrapper {:keys [anim]} _db]]
  (let [track 0
        loop true]
    (w/execute-method object-wrapper :set-animation track anim loop :update-pose true)))

(defn- generic-handler
  [[object-wrapper props _]]
  (doseq [[method params] props]
    (w/execute-method object-wrapper method params)))

(re-frame/reg-fx :generic-handler generic-handler)

; Methods which can be used as function, not re-frame effect
(def fixed-methods {:generic-handler generic-handler
                    :set-position set-position
                    :set-scale set-scale
                    :set-visibility  set-visibility
                    :set-src set-src
                    :reset-video reset-video
                    :clear-area clear-area
                    :set-filter set-filter
                    :set-data set-data
                    :set-children set-children
                    :set-skeleton set-skeleton
                    :set-combined-skin set-combined-skin
                    :set-enable set-enable
                    :set-chunks set-chunks
                    :set-anim set-anim
                    :stop stop})

(defn- change-scene-object
  "Returns fx map. Should be always empy - TODO: remove return value"
  [{:keys [db]} [_ object-name actions]]
  (let [wrappers (get-object-name db object-name)
        handlers (reduce (fn [result [action params]]
                           (if-let [fn-handler (get fixed-methods action)]
                             (update result :fn conj [fn-handler [wrappers params db]])
                             (update result :fx assoc action [wrappers params db])))
                         {:fn []
                          :fx {}}
                         actions)]
    (doseq [[fn-handler args] (:fn handlers)]
      (fn-handler args))
    (:fx handlers)))

(re-frame/reg-event-fx
  ::change-scene-object
  change-scene-object)

(re-frame/reg-event-fx
  ::set-scene-view
  (fn [{:keys [db]} [_ view-id]]
    (let [object-names (get-in db (path-to-db [:scene-layered-objects]))
          object-wrappers (->> object-names
                               (map #(get-object-name db %)))
          view (-> db
                   (get-in (path-to-db [:views]))
                   (get view-id))]
      (doseq [wrapper object-wrappers]
        (set-visibility [wrapper {:visible false}]))
      (doseq [[object-key object-patch] (:objects view)]
        (set-scene-object-state db object-key object-patch)))))

(re-frame/reg-event-fx
  ::set-scene-objects
  (fn [{:keys [db]} [_ objects]]
    (doseq [[object-key object-patch] objects]
      (set-scene-object-state db object-key object-patch))))

