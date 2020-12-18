(ns webchange.interpreter.renderer.scene.components.image.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]
    [webchange.interpreter.renderer.scene.filters.filters :as f]
    [webchange.resources.manager :as resources]
    [webchange.interpreter.renderer.scene.filters.filters :as f]))


(defn wrap
  [type name container sprite-object]
  (create-wrapper {:name          name
                   :type          type
                   :object        container
                   :set-highlight (fn [highlight]
                                    (let [highlight-filter-set (f/has-filter-by-name sprite-object "glow")]
                                      (if (and (not highlight) highlight-filter-set) (f/set-filter sprite-object "" {}))
                                      (if (and highlight (not highlight-filter-set))
                                          (f/set-filter sprite-object "glow" {}))))
                   :set-draggable (fn [draggable]
                                    (doto container
                                      (set! -interactive draggable)))
                   :set-parent (fn [parent]
                                 (.addChild (:object parent) container))
                   :set-src       (fn [src]
                                    (let [resource (resources/get-resource src)]
                                      (when (nil? resource)
                                        (throw (js/Error. (str "Resources for '" src "' were not loaded"))))
                                      (aset sprite-object "texture" (.-texture resource))))}))
