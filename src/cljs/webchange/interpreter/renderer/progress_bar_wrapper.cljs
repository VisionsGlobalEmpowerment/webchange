(ns webchange.interpreter.renderer.progress-bar-wrapper
  (:require
    [webchange.interpreter.renderer.common-wrapper :refer [create-wrapper]]))

(defn wrap
  [name {:keys [set-value]}]
  (create-wrapper {:name      name
                   :type      :progress-bar
                   :set-value set-value}))
