(ns webchange.editor-v2.diagram.modes.translation.widget-data
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.diagram.diagram-model.custom-nodes.custom-widget.colors :refer [colors]]
    [webchange.editor-v2.diagram.diagram-model.custom-nodes.custom-widget.widget-wrapper :as custom-wrapper]
    [webchange.editor-v2.graph-builder.utils.node-data :refer [get-node-type speech-node? concept-action-node?]]
    [webchange.editor-v2.translator.events :as te]
    [webchange.editor-v2.translator.translator-form.utils-play-audio :refer [play-audios-list]]
    [webchange.editor-v2.utils :refer [str->caption]]))

(def fab (r/adapt-react-class (aget js/MaterialUI "Fab")))

(defn get-node-color
  [node-data]
  (if (speech-node? node-data)
    (if (concept-action-node? node-data)
      "#FFDF82"
      "#6BC784")
    (:default colors)))

(defn- get-styles
  []
  {:title       {:margin      0
                 :padding     5
                 :text-align  "center"
                 :white-space "nowrap"}
   :text        {:margin     "0"
                 :padding    "5px"
                 :text-align "center"
                 :max-width  "230px"}
   :play-button {:right    "0"
                 :bottom   "0"
                 :position "absolute"
                 :margin   "10px"
                 :width    "36px"
                 :height   "36px"}})

(defn- header
  [props]
  (let [title (str->caption (:name props))
        phrase-text (get-in props [:data :phrase-text])
        styles (get-styles)]
    [:div
     [:h3 {:style (:title styles)}
      title]
     [:p {:style (:text styles)}
      phrase-text]]))

(defn- play-button
  [audio-data]
  (let [styles (get-styles)]
    (r/with-let [disabled? (r/atom false)]
                [fab {:style    (:play-button styles)
                      :size     "small"
                      :disabled @disabled?
                      :on-click (fn [event]
                                  (.stopPropagation event)
                                  (play-audios-list [audio-data] {:on-playing #(reset! disabled? true)
                                                                  :on-finish  #(reset! disabled? false)}))}
                 [ic/play-arrow]])))

(defn- wrapper
  [{:keys [node-data]}]
  (let [this (r/current-component)
        action-data (:data node-data)
        audio-src (or (:audio action-data)
                      (:id action-data))
        audio-data (-> action-data
                       (select-keys [:start :duration])
                       (assoc :src audio-src))]
    (into [:div {:on-click #(re-frame/dispatch [::te/set-current-selected-action node-data])
                 :style    (merge custom-wrapper/node-style
                                  {:background-color (get-node-color node-data)
                                   :padding-bottom   "30px"})}
           (when-not (nil? audio-src)
             [play-button audio-data])]
          (r/children this))))

(defn get-widget-data
  []
  {:header  header
   :wrapper wrapper})
