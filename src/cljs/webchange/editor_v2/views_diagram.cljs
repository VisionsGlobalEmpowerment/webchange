(ns webchange.editor-v2.views-diagram
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.diagram.graph-builder.graph :refer [get-diagram-graph]]
    [webchange.editor-v2.diagram.widget :refer [diagram-widget]]
    [webchange.editor-v2.subs :as editor-subs]
    [webchange.subs :as subs]))

(defn diagram
  []
  (let [scene-id (re-frame/subscribe [::subs/current-scene])
        diagram-mode (re-frame/subscribe [::editor-subs/diagram-mode])]
    (fn []
      (let [graph (-> @(re-frame/subscribe [::subs/scene @scene-id])
                      (get-diagram-graph @diagram-mode))]
        [diagram-widget {:graph graph
                         :mode  @diagram-mode}]))))
