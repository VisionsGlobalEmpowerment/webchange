(ns webchange.interpreter.renderer.scene.components.slider.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]))

(defn wrap
  [type name set-value]
  (create-wrapper {:name name
                   :type type
                   :set-value set-value}))
