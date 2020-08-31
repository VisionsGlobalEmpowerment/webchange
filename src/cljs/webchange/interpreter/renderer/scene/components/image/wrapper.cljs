(ns webchange.interpreter.renderer.scene.components.image.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]
    [webchange.interpreter.renderer.scene.filters.filters :refer [apply-filters]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.resources :as resources]))

(defn wrap
  [type name container sprite-object]
  (create-wrapper {:name           name
                   :type           type
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
