(ns webchange.interpreter.renderer.scene.components.progress.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]))

(defn wrap
  [type name {:keys [set-value]}]
  (create-wrapper {:name      name
                   :type      type
                   :set-value set-value}))
