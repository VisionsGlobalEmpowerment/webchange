(ns webchange.editor-v2.diagram-utils.modes.translation.widget-config-button
  (:require
    [cljs-react-material-ui.icons :as ic]
    [reagent.core :as r]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.translator.text.chunks :as chunks]))

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
  [node-data]
  (let [action-data (:data node-data)
        type (-> action-data :type keyword)
        styles (get-styles)]
    (when (= :text-animation type)
      [fab {:style    (:config-button styles)
            :size     "small"
            :on-click (fn [event]
                        (.stopPropagation event)
                        (re-frame/dispatch [::translator-form.actions/set-current-phrase-action node-data])
                        (re-frame/dispatch [::chunks/open action-data]))}
       [ic/settings]])))
