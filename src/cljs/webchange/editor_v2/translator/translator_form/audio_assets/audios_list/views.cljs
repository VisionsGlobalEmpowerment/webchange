(ns webchange.editor-v2.translator.translator-form.audio-assets.audios-list.views
  (:require
    [webchange.editor-v2.translator.translator-form.audio-assets.audios-list.audios-list-item.views :refer [audios-list-item]]))

(defn audios-list
  [{:keys [audios]}]
  [:div
   (for [audio-data audios]
     ^{:key (:url audio-data)}
     [audios-list-item audio-data])])
