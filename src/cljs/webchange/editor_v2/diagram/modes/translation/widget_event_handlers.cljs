(ns webchange.editor-v2.diagram.modes.translation.widget-event-handlers
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.events :as te]))

(defn get-node-color
  [node-data]
  (let [speech-node? (some #{(:type node-data)} ["audio"
                                                 "animation-sequence"])]
    (if speech-node? "#6BC784" nil)))

(defn get-widget-event-handlers
  []
  {:get-node-custom-color get-node-color
   :on-click #(re-frame/dispatch [::te/set-current-selected-action %])})
