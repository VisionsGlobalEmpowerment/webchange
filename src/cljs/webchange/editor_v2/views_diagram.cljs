(ns webchange.editor-v2.views-diagram
  (:require
    ["@projectstorm/react-diagrams" :refer [DiagramWidget]]
    [re-frame.core :as re-frame]
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.diagram.diagram-builder :refer [get-diagram-engine reorder-diagram zoom-to-fit]]
    [webchange.subs :as subs]
    [webchange.editor-v2.subs :as editor-subs]))

(defn toolbar
  []
  [:div {:style {:position         "absolute"
                 :top              0
                 :left             0
                 :z-index          10
                 :background-color "rgba(0, 0, 0, 0.2)"
                 :width            "100%"
                 :height           48}}
   [ui/button {:variant "contained"
               :style   {:margin 8}
               :on-click #(reorder-diagram)}
    "Reorder"]
   [ui/button {:variant "contained"
               :style   {:margin 8}
               :on-click #(zoom-to-fit)} "Fit zoom"]
   ])

(defn diagram
  []
  (let [scene-id (re-frame/subscribe [::subs/current-scene])
        diagram-mode (re-frame/subscribe [::editor-subs/diagram-mode])]
    (fn []
      (let [scene-data @(re-frame/subscribe [::subs/scene @scene-id])
            engine (get-diagram-engine scene-data @diagram-mode)]
        [:div.diagram-container
         [toolbar]
         [:> DiagramWidget {:diagramEngine engine}]]))))
