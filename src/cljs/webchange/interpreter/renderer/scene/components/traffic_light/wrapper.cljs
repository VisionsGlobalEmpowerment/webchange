(ns webchange.interpreter.renderer.scene.components.traffic-light.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]))

(defn wrap
  [type name container lights]
  (create-wrapper {:name   name
                   :type   type
                   :object container
                   :set-traffic-light (fn [color]
                                        (doseq [[light-name light-container] lights]
                                          (utils/set-visibility light-container (= (keyword color) light-name))))}))
