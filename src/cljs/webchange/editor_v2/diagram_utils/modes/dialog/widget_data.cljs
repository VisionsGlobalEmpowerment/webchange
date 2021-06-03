(ns webchange.editor-v2.diagram-utils.modes.dialog.widget-data
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.creation-progress.translation-progress.validate-action :refer [validate-phrase-action]]
    [webchange.editor-v2.creation-progress.warning-icon :refer [warning-icon]]
    [webchange.editor-v2.diagram-utils.diagram-model.custom-nodes.custom-widget.colors :refer [colors]]
    [webchange.editor-v2.diagram-utils.diagram-model.custom-nodes.custom-widget.widget-wrapper :as custom-wrapper]
    [webchange.editor-v2.diagram-utils.modes.dialog.widget-menu :refer [menu]]
    [webchange.editor-v2.diagram-utils.modes.dialog.widget-phrase :refer [phrase]]
    [webchange.editor-v2.diagram-utils.modes.dialog.widget-play-button :refer [play-button]]
    [webchange.editor-v2.diagram-utils.modes.translation.widget-config-button :refer [config-button]]
    [webchange.editor-v2.diagram-utils.modes.translation.widget-loop-icon :refer [loop-icon]]
    [webchange.editor-v2.graph-builder.utils.node-data :refer [speech-node? concept-action-node? get-node-tags get-node-type]]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.translator.translator-form.state.form :as translator-form]
    [webchange.editor-v2.dialog.utils.dialog-action :refer [get-inner-action]]))

(defn get-node-speech
  [node-data]
  (let [action-data (:data node-data)]
    {:data (get-inner-action action-data)}))

(defn effect-action?
  [node-data]
  (let [node-type (get-node-type node-data)
        node-tags (get-node-tags node-data)]
    (or (some #{"effect"} node-tags)  ;; preferable way
        (= node-type "action"))))     ;; legacy

(defn get-node-color
  [node-data]
  (let [speech (get-node-speech node-data)
        is-speech-node? (speech-node? speech)
        is-effect-action? (effect-action? speech)
        is-concept-action-node? (concept-action-node? node-data)]
    (if is-speech-node?
      (if is-concept-action-node?
        "#FFDF82"
        "#6BC784"
        )
      (if is-effect-action?
        (if is-concept-action-node?
          "#FFAF52"
          "#9BC784")
        (:default colors)))))

(defn- get-styles
  [node-data]
  {:node   {:background-color (get-node-color node-data)
            :padding-bottom   "30px"}
   :header {:display "flex"}})

(defn- header
  [{:keys [node-data] :as node}]
  (let [{:keys [context-menu]} @(re-frame/subscribe [::translator-form/components-settings :diagram])
        styles (get-styles node-data)]
    [:div {:style (:header styles)}
     [phrase node]
     (when-not (:hide? context-menu)
       [menu node])]))

(defn- get-target-node
  [node-data]
  (let [empty-action-pair-node? (and (->> node-data :data :type (= "sequence-data"))
                                     (->> node-data :data :data count (= 2))
                                     (->> node-data :data :data first :type (= "empty")))]
    (if empty-action-pair-node?
      (->> {:data (-> node-data :data :data second)
            :path (-> node-data :path (concat [1]))})
      (->> (str "Dialog node data not supported") js/Error. throw))))

(defn- wrapper
  [{:keys [node-data]}]
  (let [valid-node? (validate-phrase-action (:data node-data))
        this (r/current-component)
        styles (get-styles node-data)]
    (into [:div {:on-click #(re-frame/dispatch [::translator-form.actions/set-current-phrase-action node-data])
                 :style    (merge custom-wrapper/node-style
                                  (:node styles))}
           [play-button node-data]
           [loop-icon node-data]
           [config-button {:node-data       node-data
                           :get-target-node get-target-node
                           :styles          {:config-button {:top    nil
                                                             :bottom 0}}}]
           (when-not valid-node?
             [warning-icon {:styles {:main {:position "absolute"
                                            :bottom   "2px"}}}])]
          (r/children this))))

(defn get-widget-data
  []
  {:header  header
   :wrapper wrapper})
