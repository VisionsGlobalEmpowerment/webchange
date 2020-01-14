(ns webchange.editor-v2.views-diagram
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.graph-builder.graph :refer [get-diagram-graph]]
    [webchange.editor-v2.graph-builder.utils.root-nodes :refer [get-root-nodes]]
    [webchange.editor-v2.diagram.widget :refer [diagram-widget]]
    [webchange.editor-v2.subs :as editor-subs]
    [webchange.subs :as subs]))

(defn diagram
  []
  (let [scene-id (re-frame/subscribe [::subs/current-scene])
        diagram-mode (re-frame/subscribe [::editor-subs/diagram-mode])
        root (r/atom nil)]
    (fn []
      (let [graph (-> @(re-frame/subscribe [::subs/scene @scene-id])
                      (get-diagram-graph @diagram-mode {:start-node (if (= @root :all) nil @root)}))
            roots (-> @(re-frame/subscribe [::subs/scene @scene-id])
                      (get-diagram-graph :default)
                      (get-root-nodes))]
        [diagram-widget {:graph graph
                         :mode  @diagram-mode
                         :root-selector {:on-changed #(reset! root %)
                                         :values (concat [:all] roots)
                                         :current-value @root}}]))))
