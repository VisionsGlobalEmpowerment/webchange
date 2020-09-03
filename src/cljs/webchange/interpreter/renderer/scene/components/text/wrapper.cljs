(ns webchange.interpreter.renderer.scene.components.text.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]))

(defn wrap
  [type name text]
  (create-wrapper {:name     name
                   :type     type
                   :object   text
                   :set-text (fn [value]
                               (aset text "text" value))}))
