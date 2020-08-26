(ns webchange.interpreter.renderer.slider-wrapper
  (:require
    [webchange.interpreter.renderer.common-wrapper :refer [create-wrapper]]))

(defn wrap
  [name set-value]
  (create-wrapper {:name name
                   :type :slider
                   :set-value set-value}))
