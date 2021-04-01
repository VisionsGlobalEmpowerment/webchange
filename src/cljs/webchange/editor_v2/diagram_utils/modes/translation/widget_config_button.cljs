(ns webchange.editor-v2.diagram-utils.modes.translation.widget-config-button
  (:require
    [cljs-react-material-ui.icons :as ic]
    [reagent.core :as r]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.text-animation-editor.state :as chunks]
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
  (let [node (get-target-node node-data)
        action-data (:data node)
        action-type (-> action-data :type keyword)
        styles (-> (get-styles)
                   (deep-merge styles))]
    (when (= :text-animation action-type)
      [fab {:style    (:config-button styles)
            :size     "small"
            :on-click (fn [event]
                        (.stopPropagation event)
                        (re-frame/dispatch [::translator-form.actions/set-current-phrase-action node-data])
                        (re-frame/dispatch [::chunks/open]))}
       [ic/settings]])))
