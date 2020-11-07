(ns webchange.interpreter.renderer.scene.components.image.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]
    [webchange.resources.manager :as resources]))

(defn wrap
  [type name container sprite-object]
  (create-wrapper {:name             name
                   :type             type
                   :object           container
                   :set-src          (fn [src]
                                       (let [resource (resources/get-resource src)]
                                         (when (nil? resource)
                                           (throw (js/Error. (str "Resources for '" src "' were not loaded"))))
                                         (aset sprite-object "texture" (.-texture resource))))}))
