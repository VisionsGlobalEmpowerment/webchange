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
                   :set-glow-pulsation      (fn [params]
                                              (if params
                                                (f/set-filter container "glow-pulsation" params)
                                                (f/remove-filter container "glow-pulsation")))
                   :set-permanent-pulsation (fn [permanent-pulsation]
                                              (let [pulsation-filter-set (f/has-filter-by-name container "pulsation")]
                                                (if (and (not permanent-pulsation) pulsation-filter-set) (f/set-filter container "" {}))
                                                (if (and permanent-pulsation (not pulsation-filter-set))
                                                  (f/set-filter container "pulsation" (assoc permanent-pulsation :no-interval true)))))
                   :set-on-click-handler    #(utils/set-handler sprite-object "click" %)
                   :set-draggable           (fn [draggable]
                                              (doto container
                                                (set! -interactive draggable)))
                   :set-parent              (fn [parent]
                                              (.addChild (:object parent) container))
                   :set-position            (fn [position]
                                              (utils/set-position container position))
                   :set-scale               (fn [new-scale]
                                              (let [{:keys [offset scale]} @state
                                                    position (utils/get-position container)]
                                                (utils/set-position container (-> position
                                                                                  (update :x - (* (:x offset) (- (:x new-scale) (:x scale))))
                                                                                  (update :y - (* (:y offset) (- (:y new-scale) (:y scale)))))))
                                              (swap! state assoc :scale new-scale)
                                              (image-utils/set-image-size container sprite-object @state)
                                              (utils/emit container "scaleChanged" new-scale))
                   :set-image-size          (fn [image-size]
                                              (swap! state assoc :image-size image-size)
                                              (image-utils/set-image-size container sprite-object @state)
                                              (utils/emit container "scaleChanged"))
                   :set-src                 (fn [src]
                                              (if-not (empty? src)
                                                (resources/get-or-load-resource
                                                 src
                                                 {:on-complete (fn [resource]
                                                                 (let [texture (.-texture resource)]
                                                                   (aset sprite-object "texture" texture)
                                                                   (image-utils/set-image-size container sprite-object @state)
                                                                   (image-utils/set-image-position sprite-object @state)
                                                                   (image-utils/apply-boundaries container @state)
                                                                   (image-utils/apply-origin container @state)
                                                                   (utils/emit container "srcChanged")
                                                                   (aset sprite-object "visible" true)))})
                                                (aset sprite-object "visible" false)))}))
