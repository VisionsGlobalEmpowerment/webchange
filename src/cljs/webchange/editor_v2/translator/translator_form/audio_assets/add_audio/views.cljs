(ns webchange.editor-v2.translator.translator-form.audio-assets.add-audio.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.record.views :refer [audio-record-panel]]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.upload.views :refer [audio-upload-panel]]))

(defn- get-styles
  []
  {:card-wrapper {:margin "16px 0"}
   :card-content {:text-align      "center"
                  :display         "flex"
                  :justify-content "center"
                  :align-items     "center"}
   :audio-panel  {:width "50%"}})

(defn add-audio-form
  []
  (let [styles (get-styles)]
    [ui/card {:style (:card-wrapper styles)}
     [ui/card-content {:style (:card-content styles)}
      [:div {:style (:audio-panel styles)} [audio-upload-panel]]
      [:div {:style (:audio-panel styles)} [audio-record-panel]]]]))
