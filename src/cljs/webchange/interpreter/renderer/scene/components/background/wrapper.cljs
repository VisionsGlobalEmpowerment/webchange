(ns webchange.interpreter.renderer.scene.components.background.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]
    [webchange.interpreter.renderer.resources :as resources]))

(defn wrap
  [type name object]
  (create-wrapper {:name name
                   :type type
                   :object object
                   :set-src          (fn [src]
                                       (let [resource (resources/get-resource src)]
                                         (when (nil? resource)
                                           (throw (js/Error. (str "Resources for '" src "' were not loaded"))))
                                         (aset object "texture" (.-texture resource))))}))
