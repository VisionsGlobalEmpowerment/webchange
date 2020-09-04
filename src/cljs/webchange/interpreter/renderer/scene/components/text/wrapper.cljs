(ns webchange.interpreter.renderer.scene.components.text.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]))

(defn wrap
  [type name text chunks]
  (create-wrapper {:name     name
                   :type     type
                   :object   text
                   :chunks   chunks
                   :set-text (fn [value]
                               (aset text "text" value))}))
