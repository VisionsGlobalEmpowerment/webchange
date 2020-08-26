(ns webchange.interpreter.renderer.image-wrapper
  (:require
    [webchange.interpreter.renderer.common-wrapper :refer [create-wrapper]]
    [webchange.interpreter.renderer.filters :refer [apply-filters]]
    [webchange.interpreter.renderer.image-utils :as utils]))

(defn wrap
  [name container sprite-object]
  (create-wrapper {:name       name
                   :type       :image
                   :set-scale  (fn [scale]
                                 (utils/set-scale container scale))
                   :add-filter (fn [filter-data]
                                 (apply-filters container [filter-data]))
                   :set-visibility (fn [visible?]
                                 (utils/set-visibility container visible?))}))
