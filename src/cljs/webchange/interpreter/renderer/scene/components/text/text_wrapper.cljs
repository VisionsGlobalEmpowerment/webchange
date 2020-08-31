(ns webchange.interpreter.renderer.scene.components.text.text-wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]))

(defn wrap
  [name text]
  (create-wrapper {:name     name
                   :type     :text
                   :set-text (fn [value]
                               (aset text "text" value))}))
