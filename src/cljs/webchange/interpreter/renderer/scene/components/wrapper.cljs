(ns webchange.interpreter.renderer.scene.components.wrapper
  (:require
    [webchange.interpreter.pixi :refer [Container Graphics Sprite WHITE]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.components.debug :as debug]
    [webchange.interpreter.renderer.scene.filters.filters :as filters]
    [webchange.logger.index :as logger]))

(defn- with-debug
  [wrapped-object]
  (when false                                               ;(= "animated-svg-path" (:type wrapped-object))
    (let [object (:object wrapped-object)]
      (debug/show-bounds object)))
  wrapped-object)

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
        init-position (utils/get-position main-display-object)
        ]
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
                                 (utils/set-scale main-display-object scale))
            :set-visibility    (fn [visible?]
                                 (utils/set-visibility main-display-object visible?))
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
            :set-interactive   (fn [interactive?]
                                 (set! main-display-object -interactive interactive?)
                                 (set! main-display-object -interactiveChildren interactive?))
            :get-wrapped-props (fn [] [])}
           wrapper-object)))

(defn create-wrapper
  [wrapper-object]
  (-> wrapper-object
      (with-debug)
      (check-name-prop)
      (check-type-prop)
      (check-object-prop)
      (add-default-methods)))
