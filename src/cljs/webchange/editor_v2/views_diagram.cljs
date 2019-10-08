(ns webchange.editor-v2.views-diagram
  (:require
    ["@projectstorm/react-diagrams" :refer [DiagramWidget]]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.diagram.diagram-builder :refer [get-diagram-engine]]
    [webchange.subs :as subs]))

(defn diagram
  []
  (let [scene-id (re-frame/subscribe [::subs/current-scene])]
    (fn []
      (let [scene-data @(re-frame/subscribe [::subs/scene @scene-id])
            engine (get-diagram-engine scene-data)]
        [:div.diagram-container
         [:> DiagramWidget {"diagramEngine" engine}]]))))
