(ns webchange.editor-v2.translator.translator-form.audio-assets.add-audio.record.views-stop
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.events :as events]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.record.utils-recorder :as recorder]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.record.views-layout :refer [panel-layout]]))

(defn- get-styles
  []
  {:stop-record-button  {:padding       "11px"
                         :border-radius "50%"
                         :position      "relative"}
   :stop-record-icon    {:color     "#ff0000"
                         :font-size "98px"
                         :animation "pulse 1.5s infinite"}})

(defn stop-record-panel
  [{:keys [audio-blob]}]
  (let [handle-click (fn [] (recorder/stop #(do (reset! audio-blob %)
                                                (re-frame/dispatch [::events/show-play-record-panel]))))
        styles (get-styles)]
    [panel-layout
     [ui/button {:on-click handle-click
                 :style    (:stop-record-button styles)}
      [ic/fiber-manual-record {:style (:stop-record-icon styles)}]]]))
