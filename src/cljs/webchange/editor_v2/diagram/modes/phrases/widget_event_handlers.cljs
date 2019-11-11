(ns webchange.editor-v2.diagram.modes.phrases.widget-event-handlers
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.events :as ee]))

(defn get-node-color
  [node-data]
  (let [phrase-node? (contains? (:data node-data) :phrase)]
    (if phrase-node? "#6BC784" nil)))

(defn get-widget-event-handlers
  []
  {:get-node-custom-color get-node-color
   :on-double-click #(re-frame/dispatch [::ee/show-translator-form %])})
