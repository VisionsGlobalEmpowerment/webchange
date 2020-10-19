(ns webchange.interpreter.renderer.scene.components.traffic-light.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]))

(defn wrap
  [type name container]
  (create-wrapper {:name   name
                   :type   type
                   :object container}))
