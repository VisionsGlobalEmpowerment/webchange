(ns webchange.interpreter.renderer.scene.components.layered-background.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]))

(defn wrap
  [type name object]
  (create-wrapper {:name name
                   :type type
                   :object object}))
