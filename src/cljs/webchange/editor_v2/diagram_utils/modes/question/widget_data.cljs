(ns webchange.editor-v2.diagram-utils.modes.question.widget-data
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.creation-progress.translation-progress.validate-action :refer [validate-phrase-action]]
    [webchange.editor-v2.creation-progress.warning-icon :refer [warning-icon]]
    [webchange.editor-v2.diagram-utils.diagram-model.custom-nodes.custom-widget.colors :refer [colors]]
    [webchange.editor-v2.diagram-utils.diagram-model.custom-nodes.custom-widget.widget-wrapper :as custom-wrapper]
    [webchange.editor-v2.diagram-utils.modes.dialog.widget-menu :refer [menu]]
    [webchange.editor-v2.diagram-utils.modes.question.widget-phrase :refer [phrase]]
    [webchange.editor-v2.diagram-utils.modes.question.widget-play-button :refer [play-button]]
    [webchange.editor-v2.diagram-utils.modes.question.widget-config-button :refer [config-button]]
    [webchange.editor-v2.diagram-utils.modes.translation.widget-loop-icon :refer [loop-icon]]
    [webchange.editor-v2.graph-builder.utils.node-data :refer [speech-node? concept-action-node? get-node-type]]
    [webchange.editor-v2.question.question-form.state.actions :as question-form.actions]
    [webchange.editor-v2.dialog.utils.dialog-action :refer [get-inner-action]]))

(defn get-node-color
  [node-data]
  (let [type (get-in node-data [:data :type])
        dialog? (if (= type :dialog) true false)
        question? (if (= type :question) true false)]
    (if dialog?
      "#6BC784"
      (if question?
        "#FFAF52"
        "#9BC784"))))

(defn- get-styles
  [node-data]
  {:node   {:background-color (get-node-color node-data)
            :padding-right    "54px"}
   :header {:display       "flex"
            :max-height    "128px"
            :overflow      "auto"
            :text-overflow "ellipsis"}})

(defn- header
  [{:keys [node-data] :as node}]
  (let [styles (get-styles node-data)]
    [:div {:style (:header styles)}
     [phrase node]]))

(defn- wrapper
  [{:keys [node-data]}]
  (let [this (r/current-component)
        styles (get-styles node-data)]
    (into [:div {:on-click #(re-frame/dispatch [::question-form.actions/set-current-question-action node-data])
                 :on-wheel #(.stopPropagation %)
                 :style    (merge custom-wrapper/node-style
                                  (:node styles))}
           [play-button node-data]
           [config-button {:node-data node-data}]]
          (r/children this))))

(defn get-widget-data
  []
  {:header  header
   :wrapper wrapper})
