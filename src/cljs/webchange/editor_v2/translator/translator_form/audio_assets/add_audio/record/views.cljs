(ns webchange.editor-v2.translator.translator-form.audio-assets.add-audio.record.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.subs :as subs]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.record.views-params :refer [record-params-panel]]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.record.views-play :refer [play-record-panel]]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.record.views-start :refer [start-record-panel]]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.record.views-stop :refer [stop-record-panel]]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.record.views-layout :refer [panel-layout]]))

(defn audio-record-panel
  []
  (r/with-let [audio-blob (atom nil)]
              (let [current-state @(re-frame/subscribe [::subs/record-panel-state])]
                (case current-state
                  :start-record [start-record-panel]
                  :stop-record [stop-record-panel {:audio-blob audio-blob}]
                  :play-record [play-record-panel {:audio-blob @audio-blob}]
                  :set-params [record-params-panel {:audio-blob @audio-blob}]
                  [:div "..."]))))
