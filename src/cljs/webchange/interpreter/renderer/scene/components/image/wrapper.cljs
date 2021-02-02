(ns webchange.interpreter.renderer.scene.components.image.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.image.utils :as image-utils]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]
    [webchange.interpreter.renderer.scene.filters.filters :as f]
    [webchange.resources.manager :as resources]))


(defn wrap
  [type name container sprite-object state]
  (create-wrapper {:name                    name
                   :type                    type
                   :object                  container
                   :set-highlight           (fn [highlight]
                                              (let [highlight-filter-set (f/has-filter-by-name sprite-object "glow")]
                                                (if (and (not highlight) highlight-filter-set) (f/set-filter sprite-object "" {}))
                                                (if (and highlight (not highlight-filter-set))
                                                  (f/set-filter sprite-object "glow" {}))))
                   :set-permanent-pulsation (fn [permanent-pulsation]
                                              (let [pulsation-filter-set (f/has-filter-by-name sprite-object "pulsation")]
                                                (if (and (not permanent-pulsation) pulsation-filter-set) (f/set-filter sprite-object "" {}))
                                                (if (and permanent-pulsation (not pulsation-filter-set))
                                                  (f/set-filter sprite-object "pulsation" (assoc permanent-pulsation :no-interval true))))
                                              )
                   :set-on-click-handler    #(utils/set-handler sprite-object "click" %)
                   :set-draggable           (fn [draggable]
                                              (doto container
                                                (set! -interactive draggable)))
                   :set-parent              (fn [parent]
                                              (.addChild (:object parent) container))
                   :set-position            (fn [position]
                                              (utils/set-position container position))
                   :set-src                 (fn [src]
                                              (let [resource (resources/get-resource src)]
                                                (when (nil? resource)
                                                  (throw (js/Error. (str "Resources for '" src "' were not loaded"))))
                                                (let [texture (.-texture resource)]
                                                  (aset sprite-object "texture" texture)
                                                  (image-utils/set-image-size sprite-object @state)
                                               (image-utils/apply-boundaries container @state)
                                                  (image-utils/apply-origin container @state))))}))
