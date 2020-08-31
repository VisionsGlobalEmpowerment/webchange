(ns webchange.interpreter.renderer.scene.components.slider.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]))

(defn wrap
  [name set-value]
  (create-wrapper {:name name
                   :type :slider
                   :set-value set-value}))
