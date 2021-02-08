(ns webchange.interpreter.renderer.scene.components.text.wrapper
  (:require
    [webchange.interpreter.renderer.scene.filters.filters :as f]
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]))

(defn wrap
  [type name text chunks]
  (create-wrapper {:name          name
                   :type          type
                   :object        text
                   :chunks        chunks
                   :set-text      (fn [value]
                                    (aset text "text" value))
                   :set-highlight           (fn [highlight]
                                              (let [highlight-filter-set (f/has-filter-by-name text "glow")]
                                                (if (and (not highlight) highlight-filter-set) (f/set-filter text "" {}))
                                                (if (and highlight (not highlight-filter-set))
                                                  (f/set-filter text "glow" {}))))
                   :set-fill      (fn [value]
                                    (aset (.-style text) "fill" value))
                   :get-fill      (fn []
                                    (.-fill (.-style text)))
                   :set-font-size (fn [font-size]
                                    (aset (.-style text) "fontSize" font-size))}))
