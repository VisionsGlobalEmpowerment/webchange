(ns webchange.lesson-builder.widgets.audio-add.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.widgets.audio-add.state :as state]
    [webchange.ui.index :as ui]
    [webchange.ui.widgets.index :as widgets]
    [webchange.utils.languages :refer [language-options]]))

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
  (let [current-language @(re-frame/subscribe [::state/current-language])
        handle-change #(re-frame/dispatch [::state/add-audio-asset %])]
    ^{:key current-language}
    [widgets/file {:type              "audio"
                   :text              "Upload Audio"
                   :language          current-language
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

(defn- language-selector
  []
  (let [current-language @(re-frame/subscribe [::state/current-language])
        handle-select-language #(re-frame/dispatch [::state/select-language %])]
    [ui/select {:label      "Language"
                :value      current-language
                :options    language-options
                :on-change  handle-select-language
                :class-name "language-selector" }]))

(defn audio-add
  []
  (let [current-panel @(re-frame/subscribe [::state/current-panel])]
    [:div.widget--audio-add
     [language-selector]
     (case current-panel
       "recording" [record-panel]
       [index])]))
