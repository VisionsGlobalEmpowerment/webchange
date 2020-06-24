(ns webchange.editor-v2.diagram.modes.translation.widget-data
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.diagram.diagram-model.custom-nodes.custom-widget.colors :refer [colors]]
    [webchange.editor-v2.diagram.diagram-model.custom-nodes.custom-widget.widget-wrapper :as custom-wrapper]
    [webchange.editor-v2.diagram.modes.translation.widget-menu :refer [menu]]
    [webchange.editor-v2.diagram.modes.translation.widget-phrase :refer [phrase]]
    [webchange.editor-v2.diagram.modes.translation.widget-play-button :refer [play-button]]
    [webchange.editor-v2.diagram.modes.translation.widget-config-button :refer [config-button]]
    [webchange.editor-v2.diagram.modes.translation.widget-loop-icon :refer [loop-icon]]
    [webchange.editor-v2.graph-builder.utils.node-data :refer [speech-node? concept-action-node?]]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]))

(defn get-node-color
  [node-data]
  (if (speech-node? node-data)
    (if (concept-action-node? node-data)
      "#FFDF82"
      "#6BC784")
    (:default colors)))

(defn- get-styles
  [node-data]
  {:node   {:background-color (get-node-color node-data)
            :padding-bottom   "30px"}
   :header {:display "flex"}})

(defn- header
  [{:keys [node-data] :as node}]
  (let [styles (get-styles node-data)]
    [:div {:style (:header styles)}
     [phrase node]
     [menu node]]))

(defn- wrapper
  [{:keys [node-data]}]
  (let [this (r/current-component)
        styles (get-styles node-data)]
    (into [:div {:on-click #(re-frame/dispatch [::translator-form.actions/set-current-phrase-action node-data])
                 :style    (merge custom-wrapper/node-style
                                  (:node styles))}
           [play-button node-data]
           [loop-icon node-data]
           [config-button node-data]]
          (r/children this))))

(defn get-widget-data
  []
  {:header  header
   :wrapper wrapper})
