(ns webchange.interpreter.renderer.scene.components.text.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]))

(defn wrap
  [type name text chunks]
  (create-wrapper {:name          name
                   :type          type
                   :object        text
                   :chunks        chunks
                   :set-text      (fn [value]
                                    (aset text "text" value))
                   :set-fill      (fn [value]
                                    (aset (.-style text) "fill" value))
                   :get-fill      (fn []
                                    (.-fill (.-style text)))
                   :set-font-size (fn [font-size]
                                    (aset (.-style text) "fontSize" font-size))}))
