(ns webchange.interpreter.renderer.scene.components.button.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]))

(defn wrap
  [type name]
  (create-wrapper {:name name
                   :type type}))
