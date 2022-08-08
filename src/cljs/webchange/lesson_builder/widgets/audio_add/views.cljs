(ns webchange.lesson-builder.widgets.audio-add.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.widgets.audio-add.state :as state]
    [webchange.ui.index :as ui]
    [webchange.ui.widgets.index :as widgets]))

(defn- recording-button
  []
  (let [handle-stop-recording #(re-frame/dispatch [::state/stop-recording])]
    [ui/button {:on-click handle-stop-recording}
     "Stop Recording"]))

(defn- record-panel
  []
  (let [loading? @(re-frame/subscribe [::state/loading?])
        handle-stop-recording #(re-frame/dispatch [::state/stop-recording])]
    [:div.audio-add--record-panel
     [:span.record-panel--message (if loading? "Saving..." "Recording...")]
     [ui/button {:loading?   loading?
                 :on-click   handle-stop-recording
                 :class-name "audio-add--stop-recording-button"
                 :icon       "stop"
                 :icon-side  "left"
                 :shape      "rounded"}
      "Stop Recording"]]))

(defn- upload-file-button
  []
  (let [handle-change #(re-frame/dispatch [::state/add-audio-asset %])]
    [widgets/file {:type              "audio"
                   :text              "Upload Audio"
                   :language          "english"
                   :class-name-button "audio-add--upload-button"
                   :on-change         handle-change}]))

(defn- index
  []
  (let [handle-record-click #(re-frame/dispatch [::state/start-recording])]
    [:div.audio-add--index
     [ui/button {:icon      "record"
                 :icon-side "left"
                 :shape     "rounded"
                 :color     "blue-1"
                 :on-click  handle-record-click}
      "Record Voice"]
     [upload-file-button]]))

(defn audio-add
  []
  (let [current-panel @(re-frame/subscribe [::state/current-panel])]
    [:div.widget--audio-add
     (case current-panel
       "recording" [record-panel]
       [index])]))
