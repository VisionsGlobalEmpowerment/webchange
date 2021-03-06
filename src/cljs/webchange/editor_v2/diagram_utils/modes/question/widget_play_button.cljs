(ns webchange.editor-v2.diagram-utils.modes.question.widget-play-button
  (:require
    [cljs-react-material-ui.icons :as ic]
    [reagent.core :as r]
    [webchange.editor-v2.translator.translator-form.utils-play-audio :refer [play-audios-list]]))

(def fab (r/adapt-react-class (aget js/MaterialUI "Fab")))

(defn- get-styles
  []
  {:play-button {:right    "0"
                 :bottom   "0"
                 :position "absolute"
                 :margin   "10px"
                 :width    "36px"
                 :height   "36px"}})

(defn play-button
  [node-data]
  (let [action-data (case (get-in node-data [:data :type])
                      :question (get-in node-data [:data :action :data :audio-data])
                      :answer (get-in node-data [:data :answer :audio-data])
                      :dialog (get-in node-data [:data :action :data 0 :data 1])
                      {})
        audio-src (or (:audio action-data)
                      (:id action-data))
        audio-data (-> action-data
                       (select-keys [:start :duration])
                       (assoc :src audio-src))
        styles (get-styles)]
    (r/with-let [disabled? (r/atom false)]
                (when-not (or (nil? audio-src) (= "" audio-src))
                  [fab {:style    (:play-button styles)
                        :size     "small"
                        :disabled @disabled?
                        :on-click (fn [event]
                                    (.stopPropagation event)
                                    (play-audios-list [audio-data] {:on-playing #(reset! disabled? true)
                                                                    :on-finish  #(reset! disabled? false)}))}
                   [ic/play-arrow]]))))
