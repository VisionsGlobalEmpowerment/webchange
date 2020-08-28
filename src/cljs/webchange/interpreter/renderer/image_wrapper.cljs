(ns webchange.interpreter.renderer.image-wrapper
  (:require
    [webchange.interpreter.renderer.common-wrapper :refer [create-wrapper]]
    [webchange.interpreter.renderer.filters :refer [apply-filters]]
    [webchange.interpreter.renderer.common-utils :as utils]
    [webchange.interpreter.resources-manager.loader :as resources]))

(defn wrap
  [name container sprite-object]
  (create-wrapper {:name           name
                   :type           :image
                   :set-src        (fn [src]
                                     (let [resource (resources/get-resource src)]
                                       (when (nil? resource)
                                         (throw (js/Error. (str "Resources for '" src "' were not loaded"))))
                                       (aset sprite-object "texture" (.-texture resource))))
                   :set-scale      (fn [scale]
                                     (utils/set-scale container scale))
                   :add-filter     (fn [filter-data]
                                     (apply-filters container [filter-data]))
                   :set-visibility (fn [visible?]
                                     (utils/set-visibility container visible?))}))
