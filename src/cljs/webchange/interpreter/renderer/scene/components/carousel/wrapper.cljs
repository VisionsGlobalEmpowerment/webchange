(ns webchange.interpreter.renderer.scene.components.carousel.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]))

(defn wrap
  [type name container state]
  (create-wrapper {:name   name
                   :type   type
                   :object container
                   :state  state
                   :set-speed (fn [speed]
                                (swap! state assoc :speed speed))}))
