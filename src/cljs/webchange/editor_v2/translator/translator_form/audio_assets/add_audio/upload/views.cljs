(ns webchange.editor-v2.translator.translator-form.audio-assets.add-audio.upload.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.components.file-input.views :refer [select-file-form]]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.common.views-audio-params :refer [audio-params-panel]]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.state.select-method :as add-audio.select-method]
    [webchange.editor-v2.translator.translator-form.state.audios :as translator-form.audios]))

(defn- get-styles
  []
  {:wrapper          {:width "100%"}
   :button           {:width  "100%"
                      :height "190px"}
   :button-drag-over {:width  "100%"
                      :height "190px"}})

(defn audio-upload-panel
  []
  (r/with-let [selected-file (r/atom nil)]
              (if (nil? @selected-file)
                [select-file-form {:text      "Select File To Upload or Drop It Here"
                                   :on-change #(do (reset! selected-file %))
                                   :styles    (get-styles)}]
                [audio-params-panel {:on-save   (fn [params]
                                                  (re-frame/dispatch [::translator-form.audios/upload-audio @selected-file params])
                                                  (re-frame/dispatch [::add-audio.select-method/show-select-method-panel]))
                                     :on-cancel (fn []
                                                  (re-frame/dispatch [::add-audio.select-method/show-select-method-panel]))}])))
