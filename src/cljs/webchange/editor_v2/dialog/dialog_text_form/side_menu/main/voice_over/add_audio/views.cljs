(ns webchange.editor-v2.dialog.dialog-text-form.side-menu.main.voice-over.add-audio.views
  (:require
    [reagent.core :as r]
    [webchange.editor-v2.dialog.components.record-audio.views :refer [record-audio]]
    [webchange.editor-v2.dialog.dialog-form.views-upload-audio :refer [upload-audio]]))

(defn add-audio
  []
  (r/with-let [show-upload-form? (r/atom true)
               handle-start-record #(reset! show-upload-form? false)
               handle-stop-record #(reset! show-upload-form? true)]
    [:div.add-audio
     (into [:div.add-audio-form
            [:div.input-label.thin "Record voice"]
            [record-audio {:on-start-record handle-start-record
                           :on-stop-record  handle-stop-record}]]
           (when @show-upload-form?
             [[:div.input-label.thin "or"]
              [upload-audio {:input-props {:show-icon?   false
                                           :show-input?  false
                                           :button-text  "Upload file"
                                           :button-props {:color   "default"
                                                          :variant "outlined"}}}]]))]))
