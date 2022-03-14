(ns webchange.interpreter.renderer.state.scene
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.renderer.state.db :refer [path-to-db]]
    [webchange.interpreter.renderer.scene.components.wrapper-interface :as w]
    [webchange.logger.index :as logger]))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [db]} [_]]
    (let [objects (get-in db (path-to-db [:objects]) (atom {}))
          groups (get-in db (path-to-db [:groups]) (atom {}))]
      (reset! objects {})
      (reset! groups {})
      {:db (-> db
               (assoc-in (path-to-db [:objects]) objects)
               (assoc-in (path-to-db [:groups]) groups))})))

(re-frame/reg-sub
  ::rendering?
  (fn [db]
    (get-in db (path-to-db [:rendering?]) false)))

(re-frame/reg-event-fx
  ::set-rendering-state
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db (path-to-db [:rendering?]) value)}))

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

(def available-actions
  [{:action :set-align :params [:align]}
   {:action :set-image-size :params [:image-size]}
   {:action :set-position :params [:x :y]}
   {:action :set-scale :params [:scale :scale-x :scale-y]}
   {:action :set-visibility :params [:visible]}
   {:action :set-src :params [:src]}
   {:action :reset-video :params [:src]}
   {:action :set-text :params [:text]}
   {:action :clear-area :params []}
   {:action :set-filter :params [:filter :brightness :eager]}
   {:action :set-opacity :params [:opacity]}
   {:action :set-tool :params [:tool]}
   {:action :set-color :params [:color]}
   {:action :set-stroke :params [:stroke]}
   {:action :set-data :params [:data]}
   {:action :set-path :params [:path]}
   {:action :set-fill :params [:fill]}
   {:action :set-border-color :params [:border-color]}
   {:action :set-highlight :params [:highlight]}
   {:action :set-permanent-pulsation :params [:permanent-pulsation]}
   {:action :set-alpha-pulsation :params [:alpha-pulsation]}
   {:action :set-draggable :params [:draggable]}
   {:action :set-children :params [:children]}
   {:action :set-font-size :params [:font-size]}
   {:action :set-font-family :params [:font-family]}
   {:action :set-skeleton :params [:name] :accompany-params [:skin :skin-names]}
   {:action :set-animation-skin :params [:skin]}
   {:action :set-combined-skin :params [:skin-names]}
   {:action :set-enable :params [:enable?]}])

(defn- get-action-params
  [{:keys [params accompany-params] :or {params [] accompany-params []}} overall-params]
  ":params - used to get action params and determine if action is needed for current params set
   :accompany-params - also needed for action but NOT used to determine if action is needed.
   For example apply params 'name', 'skin' and 'skin-names' to 'set-skeleton' action only if 'name' is defined.
   If 'skin' is defined but 'name' is undefined, use 'set-animation-skin' action instead."
  (let [action-params (select-keys overall-params params)
        action-used? (-> action-params empty? not)]
    (if action-used?
      {:action-used?  action-used?
       :action-params (merge action-params (select-keys overall-params accompany-params))
       :rest-params   (apply dissoc overall-params (concat params accompany-params))}
      {:action-used?  action-used?
       :action-params {}
       :rest-params   overall-params})))

(re-frame/reg-event-fx
  ::set-scene-object-state
  (fn [{:keys [_]} [_ object-name state]]
    (let [filtered-state (filter-extra-props state [:revert :start :target :volume])
          [execute-actions not-handled-params] (loop [actions-to-execute []
                                                      [{:keys [action] :as available-action} & rest-available-actions] available-actions
                                                      current-state filtered-state]
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
                                not-handled-params)]
      {:dispatch [::change-scene-object
                  object-name
                  (cond-> execute-actions
                          (-> generic-handlers empty? not) (conj [:generic-handler generic-handlers]))]})))

(defn get-object-name
  [db object-name]
  (or (get-scene-object db object-name)
      (get-scene-group db object-name)))

(re-frame/reg-event-fx
  ::change-scene-object
  (fn [{:keys [db]} [_ object-name actions]]
    (let [wrappers (get-object-name db object-name)]
      (->> actions
           (map (fn [[action params]] [action [wrappers params db]]))
           (into {})))))

(defn- apply-to-wrapper
  [method target & params]
  (let [targets (if (sequential? target) target [target])]
    (doseq [wrapper targets]
      (apply method (concat [wrapper] params)))))

(re-frame/reg-fx
  :add-filter
  (fn [[object-wrapper filter-data]]
    (apply-to-wrapper w/add-filter object-wrapper filter-data)))

(re-frame/reg-fx
  :set-filter
  (fn [[object-wrapper {:keys [filter] :as params}]]
    (apply-to-wrapper w/set-filter object-wrapper filter params)))

(re-frame/reg-fx
  :set-align
  (fn [[object-wrapper {:keys [align]}]]
    (apply-to-wrapper w/set-align object-wrapper align)))

(re-frame/reg-fx
  :set-image-size
  (fn [[object-wrapper {:keys [image-size]}]]
    (apply-to-wrapper w/set-image-size object-wrapper image-size)))

(re-frame/reg-fx
  :set-position
  (fn [[object-wrapper position]]
    (apply-to-wrapper w/set-position object-wrapper position)))

(re-frame/reg-fx
  :set-scale
  (fn [[object-wrapper scale]]
    (apply-to-wrapper w/set-scale object-wrapper (or (:scale scale) scale))))

(re-frame/reg-fx
  :set-visibility
  (fn [[object-wrapper {:keys [visible]}]]
    (apply-to-wrapper w/set-visibility object-wrapper visible)))

(re-frame/reg-fx
  :set-value
  (fn [[object-wrapper value]]
    (apply-to-wrapper w/set-value object-wrapper value)))

(re-frame/reg-fx
  :set-text
  (fn [[object-wrapper {:keys [text]}]]
    (apply-to-wrapper w/set-text object-wrapper text)))

(re-frame/reg-fx
  :clear-area
  (fn [[object-wrapper {:keys [text]}]]
    (apply-to-wrapper w/clear-area object-wrapper text)))

(re-frame/reg-fx
  :set-font-size
  (fn [[object-wrapper {:keys [font-size]}]]
    (apply-to-wrapper w/set-font-size object-wrapper font-size)))

(re-frame/reg-fx
  :set-font-family
  (fn [[object-wrapper {:keys [font-family]}]]
    (apply-to-wrapper w/set-font-family object-wrapper font-family)))

(re-frame/reg-fx
  :set-src
  (fn [[object-wrapper {:keys [src options]}]]
    (apply-to-wrapper w/set-src object-wrapper src options)))

(re-frame/reg-fx
  :reset-video
  (fn [[object-wrapper {:keys [src options]}]]
    (apply-to-wrapper w/reset-video object-wrapper src options)))

(re-frame/reg-fx
  :set-highlight
  (fn [[object-wrapper {:keys [highlight options]}]]
    (apply-to-wrapper w/set-highlight object-wrapper highlight options)))

(re-frame/reg-fx
  :set-permanent-pulsation
  (fn [[object-wrapper {:keys [permanent-pulsation options]}]]
    (apply-to-wrapper w/set-permanent-pulsation object-wrapper permanent-pulsation options)))

(re-frame/reg-fx
  :set-alpha-pulsation
  (fn [[object-wrapper {:keys [alpha-pulsation options]}]]
    (apply-to-wrapper w/set-alpha-pulsation object-wrapper alpha-pulsation options)))

(re-frame/reg-fx
  :set-draggable
  (fn [[object-wrapper {:keys [draggable options]}]]
    (apply-to-wrapper w/set-draggable object-wrapper draggable options)))

(re-frame/reg-fx
  :set-children
  (fn [[object-wrapper {:keys [children options]} db]]
    (let [object (:object object-wrapper)
          parent (.-parent (.-parent object))
          children (vec (array-seq (.-children object)))]
      (doseq [child children]
        (.addChild parent child))

      (doseq [child children]
        (.removeChild object child)))

    (doseq [child children]
      (apply-to-wrapper w/set-parent (get-object-name db (keyword child)) object-wrapper options))))

(re-frame/reg-fx
  :set-opacity
  (fn [[object-wrapper {:keys [opacity]}]]
    (apply-to-wrapper w/set-opacity object-wrapper opacity)))

(re-frame/reg-fx
  :set-tool
  (fn [[object-wrapper {:keys [tool]}]]
    (apply-to-wrapper w/set-tool object-wrapper tool)))

(re-frame/reg-fx
  :set-color
  (fn [[object-wrapper {:keys [color]}]]
    (apply-to-wrapper w/set-color object-wrapper color)))

(re-frame/reg-fx
  :stop
  (fn [[object-wrapper]]
    (apply-to-wrapper w/stop object-wrapper)))

(re-frame/reg-fx
  :set-data
  (fn [[object-wrapper params]]
    (apply-to-wrapper w/set-data object-wrapper params)))

(re-frame/reg-fx
  :set-path
  (fn [[object-wrapper {:keys [path]}]]
    (apply-to-wrapper w/set-path object-wrapper path)))

(re-frame/reg-fx
  :set-fill
  (fn [[object-wrapper {:keys [fill]}]]
    (apply-to-wrapper w/set-fill object-wrapper fill)))

(re-frame/reg-fx
  :set-border-color
  (fn [[object-wrapper {:keys [border-color]}]]
    (apply-to-wrapper w/set-border-color object-wrapper border-color)))

(re-frame/reg-fx
  :set-stroke
  (fn [[object-wrapper params]]
    (apply-to-wrapper w/set-stroke object-wrapper params)))

(re-frame/reg-event-fx
  ::set-traffic-light
  (fn [{:keys [db]} [_ object-name color]]
    (let [wrapper (get-scene-object db object-name)]
      (apply-to-wrapper w/set-traffic-light wrapper color))))

(re-frame/reg-fx
  :set-animation-skin
  (fn [[object-wrapper {:keys [skin]}]]
    (apply-to-wrapper w/set-skin object-wrapper skin)))

(re-frame/reg-fx
  :set-combined-skin
  (fn [[object-wrapper {:keys [skin-names]}]]
    (apply-to-wrapper w/set-combined-skin object-wrapper skin-names)))

(re-frame/reg-fx
  :set-enable
  (fn [[object-wrapper {:keys [enable?]}]]
    (apply-to-wrapper w/set-enable object-wrapper enable?)))

(re-frame/reg-fx
  :set-skeleton
  (fn [[object-wrapper params]]
    (apply-to-wrapper w/set-skeleton object-wrapper params)))

(re-frame/reg-fx
  :generic-handler
  (fn [[object-wrapper props _]]
    (doseq [[method params] props]
      (apply-to-wrapper w/execute-method object-wrapper method params))))
