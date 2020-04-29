(ns webchange.editor-v2.translator.translator-form.audio-assets.add-audio.record.views-start
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.record.utils-recorder :as recorder]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.record.views-layout :refer [panel-layout]]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.state.record-panel :as add-audio.record-panel]))

(defn- get-styles
  []
  {:start-record-button {:padding       "36px"
                         :border-radius "50%"}
   :start-record-icon   {:font-size "48px"}})

(defn start-record-panel
  []
  (let [handle-click (fn [] (recorder/start #(re-frame/dispatch [::add-audio.record-panel/show-stop-record-panel])))
        styles (get-styles)]
    [panel-layout
     [ui/button {:on-click handle-click
                 :style    (:start-record-button styles)}
      [ic/mic {:style (:start-record-icon styles)}]]]))
