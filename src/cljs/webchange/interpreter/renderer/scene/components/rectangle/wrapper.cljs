(ns webchange.interpreter.renderer.scene.components.rectangle.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]))

(defn wrap
  [type name object sprite]
  (create-wrapper {:name name
                   :type type
                   :set-fill #(aset sprite "tint" %)
                   :object object}))
