(ns webchange.interpreter.renderer.scene.components.image.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]
    [webchange.interpreter.renderer.scene.filters.filters :refer [apply-filters get-filter-value set-filter-value]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.resources :as resources]))

(defn wrap
  [type name container sprite-object]
  (create-wrapper {:name             name
                   :type             type
                   :object           container
                   :set-src          (fn [src]
                                       (let [resource (resources/get-resource src)]
                                         (when (nil? resource)
                                           (throw (js/Error. (str "Resources for '" src "' were not loaded"))))
                                         (aset sprite-object "texture" (.-texture resource))))
                   :get-position     (fn []
                                       (utils/get-position container))
                   :set-position     (fn [position]
                                       (utils/set-position container position))
                   :set-scale        (fn [scale]
                                       (utils/set-scale container scale))
                   :add-filter       (fn [filter-data]
                                       (apply-filters container [filter-data]))
                   :get-filter-value (fn [filter-name]
                                       (get-filter-value container filter-name))
                   :set-filter-value (fn [filter-name value]
                                       (set-filter-value container filter-name value))
                   :set-visibility   (fn [visible?]
                                       (utils/set-visibility container visible?))}))
