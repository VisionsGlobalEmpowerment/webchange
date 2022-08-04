(ns webchange.editor-v2.translator.translator-form.audio-assets.add-audio.views-record
  (:require
    [reagent.core :as r]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [webchange.utils.audio-recorder :as recorder]
    [webchange.editor-v2.translator.translator-form.state.audios :as translator-form.audios]))

(defn- get-styles
  []
  {:start-record-button {:padding       "26px"
                         :border-radius "50%"}
   :start-record-icon   {:font-size "48px"}
   :stop-record-button  {:padding       "11px"
                         :border-radius "50%"
                         :position      "relative"}
   :stop-record-icon    {:color     "#ff0000"
                         :font-size "98px"
                         :animation "pulse 1.5s infinite"}})

(defn- start-record-panel
  [{:keys [on-click]}]
  (let [styles (get-styles)]
    [ui/button {:on-click on-click
                :style    (:start-record-button styles)}
     [ic/mic {:style (:start-record-icon styles)}]]))

(defn stop-record-panel
  [{:keys [on-click]}]
  (let [styles (get-styles)]
    [ui/button {:on-click on-click
                :style    (:stop-record-button styles)}
     [ic/fiber-manual-record {:style (:stop-record-icon styles)}]]))

(defn audio-record-panel
  []
  (r/with-let [current-state (r/atom :start-record)]
              (let [handle-start-record (fn [] (recorder/start #(reset! current-state :stop-record)))
                    handle-stop-record (fn [] (recorder/stop
                                                #(do (reset! current-state :start-record)
                                                     (re-frame/dispatch [::translator-form.audios/upload-audio %
                                                                         {} [["type" "blob"] ["blob-type" "audio"]]]))))]
                (case @current-state
                  :start-record [start-record-panel {:on-click handle-start-record}]
                  :stop-record [stop-record-panel {:on-click handle-stop-record}]
                  [:div "..."]))))
