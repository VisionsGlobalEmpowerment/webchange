(ns webchange.interpreter.renderer.scene.components.slider.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]))

(defn wrap
  [type name object set-value]
  (create-wrapper {:name name
                   :type type
                   :object object
                   :set-value set-value}))
