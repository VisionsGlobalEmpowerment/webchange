(ns webchange.editor-v2.translator.translator-form.audio-assets.add-audio.views-upload
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.components.file-input.views :refer [select-file-form]]
    [webchange.editor-v2.translator.translator-form.state.audios :as translator-form.audios]))

(defn- get-styles
  []
  (let [height "100px"]
    {:wrapper          {:width "100%"}
     :button           {:width  "100%"
                        :height height}
     :button-drag-over {:width  "100%"
                        :height height}}))

(def text-default "Select File To Upload or Drop It Here")
(def text-uploading "File Uploading...")
(def text-uploaded "File Uploaded")

(defn audio-upload-panel
  []
  (r/with-let [text (r/atom text-default)
               loading (r/atom false)]
              [select-file-form {:text      @text
                                 :loading @loading
                                 :on-change (fn [file]
                                              (reset! text text-uploading)
                                              (reset! loading true)
                                              (re-frame/dispatch [::translator-form.audios/upload-audio file {}
                                                                  {:on-success (fn []
                                                                                 (reset! loading false)
                                                                                 (reset! text text-uploaded)
                                                                                 (.setTimeout js/window #(reset! text text-default) 2000))}])
                                              )
                                 :styles    (get-styles)}]))
