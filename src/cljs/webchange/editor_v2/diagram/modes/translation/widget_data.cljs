(ns webchange.editor-v2.diagram.modes.translation.widget-data
  (:require
    [clojure.string :refer [capitalize]]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.diagram.diagram-model.custom-nodes.custom-widget.colors :refer [colors]]
    [webchange.editor-v2.diagram.diagram-model.custom-nodes.custom-widget.widget-wrapper :as custom-wrapper]
    [webchange.editor-v2.graph-builder.utils.node-data :refer [get-node-type speech-node? concept-action-node?]]
    [webchange.editor-v2.translator.translator-form.events :as te]
    [webchange.editor-v2.translator.translator-form.utils :refer [node-data->phrase-data]]
    [webchange.editor-v2.translator.translator-form.utils-play-audio :refer [play-audios-list]]))

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
  {:title        {:margin      "0"
                  :padding     "10px"
                  :text-align  "left"
                  :max-width   "250px"
                  :min-width   "180px"
                  :font-size   "1.5rem"
                  :line-height "1.28571429em"}
   :title-target {:font-size       "1.7rem"
                  :font-weight     "bold"
                  :margin-right    "16px"
                  :text-decoration "underline"}
   :play-button  {:right    "0"
                  :bottom   "0"
                  :position "absolute"
                  :margin   "10px"
                  :width    "36px"
                  :height   "36px"}})

(defn- header
  [{:keys [name] :as node}]
  (let [phrase-data (node-data->phrase-data node)
        phrase-target (when-not (nil? (:target phrase-data))
                        (-> (:target phrase-data) (capitalize) (str ":")))
        phrase-text (or (:phrase-text-translated phrase-data)
                        (:phrase-text phrase-data))
        styles (get-styles)]
    (if-not (nil? phrase-text)
      [:div {:style (:title styles)}
       (when-not (nil? phrase-target)
         [:span {:style (:title-target styles)} phrase-target])
       [:span phrase-text]]
      [:div {:style (:title styles)}
       [:p "! NO PHRASE TEXT !"]
       [:p name]])))

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
