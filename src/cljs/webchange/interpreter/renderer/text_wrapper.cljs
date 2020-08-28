(ns webchange.interpreter.renderer.text-wrapper
  (:require
    [webchange.interpreter.renderer.common-wrapper :refer [create-wrapper]]))

(defn wrap
  [name text]
  (create-wrapper {:name     name
                   :type     :text
                   :set-text (fn [value]
                               (aset text "text" value))}))
