(ns webchange.interpreter.renderer.scene.components.wrapper
  (:require
   [webchange.interpreter.renderer.scene.components.utils :as utils]
   [webchange.interpreter.renderer.scene.components.debug :refer [with-debug]]
   [webchange.interpreter.renderer.scene.filters.filters :as filters]
   [webchange.logger.index :as logger]))

(defn- check-name-prop
  [wrapper]
  (when (-> wrapper :name nil?)
    (logger/warn "Wrapped object :name is not defined"))
  wrapper)

(defn- check-type-prop
  [wrapper]
  (when (-> wrapper :type nil?)
    (logger/warn "Wrapped object :type is not defined"))
  wrapper)

(defn- check-object-prop
  [wrapper]
  (when (-> wrapper :object nil?)
    (logger/warn "Wrapped object :object is not defined"))
  wrapper)

(defn- add-default-methods
  [wrapper-object]
  (let [object-data (atom {})
        main-display-object (:object wrapper-object)
        init-position (utils/get-position main-display-object)]
    (merge {:get-object-data   (fn [] @object-data)
            :set-object-data   (fn [data]
                                 (reset! object-data data))
            :get-data          (fn []
                                 (merge (utils/get-stage-position main-display-object)))
            :get-position      (fn []
                                 (utils/get-position main-display-object))
            :get-init-position (fn [] init-position)
            :set-position      (fn [position]
                                 (utils/set-position main-display-object position))
            :get-scale         (fn []
                                 (utils/get-scale main-display-object))
            :set-scale         (fn [scale]
                                 (utils/set-scale main-display-object scale)
                                 (utils/emit main-display-object "scaleChanged" scale))
            :set-visibility    (fn [visible?]
                                 (utils/set-visibility main-display-object visible?)
                                 (utils/emit main-display-object "visibilityChanged" visible?))
            :get-visibility    (fn []
                                 (utils/get-visibility main-display-object))
            :get-rotation      (fn []
                                 (utils/get-rotation main-display-object))
            :set-rotation      (fn [value]
                                 (utils/set-rotation main-display-object value))
            :add-filter        (fn [filter-data]
                                 (filters/apply-filters main-display-object [filter-data]))
            :get-filter-value  (fn [filter-name]
                                 (filters/get-filter-value main-display-object filter-name))
            :set-filter-value  (fn [filter-name value]
                                 (filters/set-filter-value main-display-object filter-name value))
            :set-filter        (fn [filter-name params]
                                 (filters/set-filter main-display-object filter-name params))
            :get-opacity       (fn []
                                 (utils/get-opacity main-display-object))
            :set-opacity       (fn [value]
                                 (utils/set-opacity main-display-object value))
            :set-alpha-pulsation (fn [alpha-pulsation]
                                   (let [pulsation-filter-set (filters/has-filter-by-name main-display-object "alpha-pulsation")]
                                     (when (and (not alpha-pulsation) pulsation-filter-set)
                                       (filters/set-filter main-display-object "" {}))
                                     (when (and alpha-pulsation (not pulsation-filter-set))
                                       (filters/set-filter main-display-object "alpha-pulsation" (assoc alpha-pulsation :no-interval true)))))
            :set-interactive   (fn [interactive?]
                                 (set! main-display-object -interactive interactive?)
                                 (set! main-display-object -interactiveChildren interactive?))
            :get-wrapped-props (fn [] [])
            :get-bounds (fn []
                          (utils/get-bounds main-display-object))}
           wrapper-object)))

(defn- with-destroy
  [{:keys [object on-destroy] :as wrapper}]
  (when (fn? on-destroy)
    (utils/set-handler object "removed" on-destroy))
  wrapper)

(defn create-wrapper
  [wrapper-object]
  (-> wrapper-object
      (with-debug)
      (check-name-prop)
      (check-type-prop)
      (check-object-prop)
      (add-default-methods)
      (with-destroy)))
