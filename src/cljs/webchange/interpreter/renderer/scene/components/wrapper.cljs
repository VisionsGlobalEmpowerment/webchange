(ns webchange.interpreter.renderer.scene.components.wrapper
  (:require
    [clojure.set :refer [difference]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.filters.filters :as filters]
    [webchange.logger :as logger]))

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

(defn- check-interface
  [wrapper]
  (let [methods-with-interface (->> 'webchange.interpreter.renderer.scene.components.wrapper-interface
                                    (ns-publics)
                                    (keys)
                                    (map keyword)
                                    (set))
        wrapper-object-methods (->> wrapper
                                    (filter (fn [[_ value]]
                                              (fn? value)))
                                    (map first)
                                    (set))
        methods-without-interface (difference wrapper-object-methods methods-with-interface)]
    (when-not (empty? methods-without-interface)
      (logger/warn "Wrapped methods don't have interface:" (clj->js methods-without-interface)))
    wrapper))

(defn- add-default-methods
  [wrapper-object]
  (let [main-display-object (:object wrapper-object)]
    (merge {:get-data         (fn []
                                (merge (utils/get-stage-position main-display-object)))
            :get-position     (fn []
                                (utils/get-position main-display-object))
            :set-position     (fn [position]
                                (utils/set-position main-display-object position))
            :get-scale        (fn []
                                (utils/get-scale main-display-object))
            :set-scale        (fn [scale]
                                (utils/set-scale main-display-object scale))
            :set-visibility   (fn [visible?]
                                (utils/set-visibility main-display-object visible?))
            :get-rotation     (fn []
                                (utils/get-rotation main-display-object))
            :set-rotation     (fn [value]
                                (utils/set-rotation main-display-object value))
            :add-filter       (fn [filter-data]
                                (filters/apply-filters main-display-object [filter-data]))
            :get-filter-value (fn [filter-name]
                                (filters/get-filter-value main-display-object filter-name))
            :set-filter-value (fn [filter-name value]
                                (filters/set-filter-value main-display-object filter-name value))}
           wrapper-object)))

(defn- add-call-method
  [wrapper]
  (assoc wrapper
    :call
    (fn [method-name & params]
      (let [method (get wrapper method-name)]
        (if-not (nil? method)
          (apply method params)
          (logger/warn (str "Method " method-name " is not implemented for " (:type wrapper))))))))

(defn create-wrapper
  [wrapper-object]
  (-> wrapper-object
      (check-name-prop)
      (check-type-prop)
      (check-object-prop)
      (check-interface)
      (add-default-methods)
      (add-call-method)))
