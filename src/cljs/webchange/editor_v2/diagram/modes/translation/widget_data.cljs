(ns webchange.editor-v2.diagram.modes.translation.widget-data
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.graph-builder.utils.node-data :refer [get-node-type concept-action-node?]]
    [webchange.editor-v2.translator.events :as te]
    [webchange.editor-v2.diagram.diagram-model.custom-nodes.custom-widget.colors :refer [colors]]
    [webchange.editor-v2.diagram.diagram-model.custom-nodes.custom-widget.widget-wrapper :as custom-wrapper]))

(defn get-node-color
  [node-data]
  (let [speech-node? (some #{(get-node-type node-data)} ["audio"
                                                 "animation-sequence"])
        concept-node? (concept-action-node? node-data)]
    (cond
      (and speech-node? concept-node?) "#FFDF82"
      speech-node? "#6BC784"
      :else (:default colors))))

(defn wrapper
  [{:keys [node-data]}]
  (let [this (r/current-component)]
    (into [:div {:on-click #(re-frame/dispatch [::te/set-current-selected-action node-data])
                 :style    (merge custom-wrapper/node-style
                                  {:background-color (get-node-color node-data)})}]
          (r/children this))))

(defn get-widget-data
  []
  {:wrapper wrapper})
