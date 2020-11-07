(ns webchange.interpreter.renderer.scene.components.progress.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]))

(defn wrap
  [type name object {:keys [set-value]}]
  (create-wrapper {:name      name
                   :type      type
                   :object    object
                   :set-value set-value}))
