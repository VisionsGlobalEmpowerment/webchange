(ns webchange.editor-v2.translator.translator-form.audio-assets.add-audio.record.views-params
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.common.views-audio-params :refer [audio-params-panel]]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.state.record-panel :as add-audio.record-panel]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.state.select-method :as add-audio.select-method]
    [webchange.editor-v2.translator.translator-form.state.audios :as translator-form.audios]))

(defn record-params-panel
  [{:keys [audio-blob]}]
  (let [handle-save (fn [params]
                      (re-frame/dispatch [::translator-form.audios/upload-audio audio-blob params [["type" "blob"] ["blob-type" "audio"]]])
                      (re-frame/dispatch [::add-audio.select-method/show-select-method-panel]))
        handle-cancel (fn []
                        (re-frame/dispatch [::add-audio.record-panel/show-play-record-panel]))]
    [audio-params-panel {:on-save   handle-save
                         :on-cancel handle-cancel}]))
