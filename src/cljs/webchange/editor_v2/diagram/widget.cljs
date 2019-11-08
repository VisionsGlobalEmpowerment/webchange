(ns webchange.editor-v2.diagram.widget
  (:require
    ["@projectstorm/react-diagrams" :refer [DiagramWidget]]
    [cljs-react-material-ui.reagent :as ui]
    [reagent.core :as r]
    [webchange.editor-v2.diagram.diagram-model.init :refer [init-diagram-model]]
    [webchange.editor-v2.diagram.diagram-utils.utils :refer [reorder zoom-to-fit]]))

(defn toolbar
  [engine]
  [:div {:style {:position         "absolute"
                 :top              0
                 :left             0
                 :z-index          10
                 :background-color "rgba(0, 0, 0, 0.2)"
                 :width            "100%"
                 :height           48}}
   [ui/button {:variant  "contained"
               :style    {:margin 8}
               :on-click #(reorder engine)}
    "Reorder"]
   [ui/button {:variant  "contained"
               :style    {:margin 8}
               :on-click #(zoom-to-fit engine)} "Fit zoom"]])

(defn diagram-did-mount
  [this]
  (let [engine (aget this "engine")]
    (reorder engine)
    (.setTimeout js/window #(zoom-to-fit engine) 10)))

(defn diagram-render
  [{:keys [graph mode]}]
  (let [engine (init-diagram-model graph mode)
        this (r/current-component)]
    (aset this "engine" engine)
    [:div.diagram-container
     [toolbar engine]
     [:> DiagramWidget {:diagramEngine engine}]]))

(def diagram-widget
  (with-meta diagram-render
             {:component-did-mount diagram-did-mount}))
