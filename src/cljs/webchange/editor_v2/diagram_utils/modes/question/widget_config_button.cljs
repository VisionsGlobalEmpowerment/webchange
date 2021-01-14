(ns webchange.editor-v2.diagram-utils.modes.question.widget-config-button
  (:require
    [cljs-react-material-ui.icons :as ic]
    [reagent.core :as r]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.question.text.views-text-animation-editor :as chunks]
    [webchange.ui.utils :refer [deep-merge]]))

(def fab (r/adapt-react-class (aget js/MaterialUI "Fab")))

(defn- get-styles
  []
  {:config-button {:right    "0"
                   :top      "0"
                   :position "absolute"
                   :margin   "10px"
                   :width    "36px"
                   :height   "36px"}})

(defn config-button
  [{:keys [node-data get-target-node styles]
    :or   {get-target-node identity
           styles          {}}}]
  (let [node node-data
        type (get-in node [:data :type])
        audio-data (case type
                     :question (get-in node [:data :action :data :audio-data])
                     :answer (get-in node [:data :answer :audio-data])
                     {})
        node-data (case type
                    :question (get-in node [:data :action :data])
                    :answer (get-in node [:data :answer])
                    {})
        styles (-> (get-styles)
                   (deep-merge styles))]
    (when (or (= type :answer) (= type :question))
      [fab {:style    (:config-button styles)
            :size     "small"
            :on-click (fn [event]
                        (.stopPropagation event)
                        (re-frame/dispatch [::translator-form.actions/set-current-phrase-action node])
                        (re-frame/dispatch [::chunks/open audio-data node-data]))}
       [ic/settings]])))
