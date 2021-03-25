(ns webchange.editor-v2.dialog.dialog-form.views-upload-audio
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.state.audios :as translator-form.audios]
    [webchange.ui-framework.components.index :refer [file]]))

(defn upload-audio
  []
  (let [handle-change #(re-frame/dispatch [::translator-form.audios/upload-audio % {}])]
    [file {:type            "audio"
           :on-change       handle-change
           :show-file-name? false
           :with-upload?    false
           :button-text     "Choose File"}]))
