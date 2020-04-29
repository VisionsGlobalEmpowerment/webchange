(ns webchange.editor-v2.translator.translator-form.audio-assets.add-audio.record.views-params
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.common.views-audio-params :refer [audio-params-panel]]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.events :as events]
    [webchange.editor-v2.translator.translator-form.audio-assets.events :as assets-events]))

(defn record-params-panel
  [{:keys [audio-blob]}]
  (let [handle-save (fn [params]
                      (re-frame/dispatch [::assets-events/upload-audio audio-blob params [["type" "blob"]
                                                                                          ["blob-type" "audio"]]])
                      (re-frame/dispatch [::events/show-select-method-panel]))
        handle-cancel (fn []
                        (re-frame/dispatch [::events/show-play-record-panel]))]
    [audio-params-panel {:on-save   handle-save
                         :on-cancel handle-cancel}]))
