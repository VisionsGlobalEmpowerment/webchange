(ns webchange.interpreter.renderer.image-wrapper
  (:require
    [webchange.interpreter.renderer.filters :refer [apply-filters]]
    [webchange.interpreter.renderer.image-utils :as utils]))

(defn wrap
  [name container sprite-object]
  {:name       name
   :set-scale  (fn [scale]
                 (utils/set-scale container scale))
   :add-filter (fn [filter-data]
                 (apply-filters container [filter-data]))})
