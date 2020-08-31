(ns webchange.interpreter.renderer.scene.components.progress.progress-wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]))

(defn wrap
  [name {:keys [set-value]}]
  (create-wrapper {:name      name
                   :type      :progress-bar
                   :set-value set-value}))
