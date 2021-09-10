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
                                              (let [highlight-filter-set (f/has-filter-by-name container "glow")]
                                                (when (and (not highlight) highlight-filter-set)
                                                  (f/set-filter container "" {}))
                                                (when (and highlight (not highlight-filter-set))
                                                  (f/set-filter container "glow" {}))))
                   :set-permanent-pulsation (fn [permanent-pulsation]
                                              (let [pulsation-filter-set (f/has-filter-by-name container "pulsation")]
                                                (if (and (not permanent-pulsation) pulsation-filter-set) (f/set-filter container "" {}))
                                                (if (and permanent-pulsation (not pulsation-filter-set))
                                                  (f/set-filter container "pulsation" (assoc permanent-pulsation :no-interval true))))
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
                                              (when src
                                                (resources/load-resource
                                                  src
                                                  (fn [resource]
                                                    (let [texture (.-texture resource)]
                                                      (aset sprite-object "texture" texture)
                                                      (image-utils/set-image-size sprite-object @state)
                                                      (image-utils/set-image-position sprite-object @state)
                                                      (image-utils/apply-boundaries container @state)
                                                      (image-utils/apply-origin container @state)
                                                      (utils/emit container "srcChanged"))))))}))
