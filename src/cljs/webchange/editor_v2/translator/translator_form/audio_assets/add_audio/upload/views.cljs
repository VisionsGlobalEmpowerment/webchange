(ns webchange.editor-v2.translator.translator-form.audio-assets.add-audio.upload.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.components.file-input.views :refer [select-file-form]]
    [webchange.editor-v2.translator.translator-form.state.audios :as translator-form.audios]))

(defn- get-styles
  []
  (let [height "150px"]
    {:wrapper          {:width "100%"}
     :button           {:width  "100%"
                        :height height}
     :button-drag-over {:width  "100%"
                        :height height}}))

(def text-default "Select File To Upload or Drop It Here")
(def text-uploaded "File Uploaded")

(defn audio-upload-panel
  []
  (r/with-let [text (r/atom text-default)]
              [select-file-form {:text      @text
                                 :on-change (fn [file]
                                              (re-frame/dispatch [::translator-form.audios/upload-audio file {}])
                                              (reset! text text-uploaded)
                                              (.setTimeout js/window #(reset! text text-default) 2000))
                                 :styles    (get-styles)}]))
