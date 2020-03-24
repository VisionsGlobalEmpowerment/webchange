(ns webchange.editor-v2.diagram.modes.translation.widget-data
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.graph-builder.utils.node-data :refer [get-node-type speech-node? concept-action-node?]]
    [webchange.editor-v2.translator.events :as te]
    [webchange.editor-v2.diagram.diagram-model.custom-nodes.custom-widget.colors :refer [colors]]
    [webchange.editor-v2.diagram.diagram-model.custom-nodes.custom-widget.widget-wrapper :as custom-wrapper]
    [webchange.editor-v2.utils :refer [str->caption]]))

(defn get-node-color
  [node-data]
  (if (speech-node? node-data)
    (if (concept-action-node? node-data)
      "#FFDF82"
      "#6BC784")
    (:default colors)))

(defn- header
  [props]
  (let [title (str->caption (:name props))
        phrase-text (get-in props [:data :phrase-text])]
    [:div
     [:h3 {:style {:margin      0
                   :padding     5
                   :text-align  "center"
                   :white-space "nowrap"}}
      title]
     [:p {:style {:margin     "0"
                  :padding    "5px"
                  :text-align "center"
                  :max-width  "230px"}}
      phrase-text]]))

(defn- wrapper
  [{:keys [node-data]}]
  (let [this (r/current-component)]
    (into [:div {:on-click #(re-frame/dispatch [::te/set-current-selected-action node-data])
                 :style    (merge custom-wrapper/node-style
                                  {:background-color (get-node-color node-data)})}]
          (r/children this))))

(defn get-widget-data
  []
  {:header  header
   :wrapper wrapper})
