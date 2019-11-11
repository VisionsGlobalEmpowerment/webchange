(ns webchange.editor-v2.diagram.modes.translation.widget-event-handlers
  (:require
    [re-frame.core :as re-frame]))

(defn get-node-color
  [node-data]
  (let [speech-node? (some #{(:type node-data)} ["audio"
                                                 "animation-sequence"])]
    (if speech-node? "#6BC784" nil)))

(defn get-widget-event-handlers
  []
  {:get-node-custom-color get-node-color
   :on-click #(println "Speech node click")
   :on-double-click #(println "Speech node double click")})
