(ns webchange.editor-v2.translator.translator-form.audio-assets.add-audio.views
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.record.views :refer [audio-record-panel]]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.upload.views :refer [audio-upload-panel]]))

(defn- get-styles
  []
  {
   :button       {:margin "0 16px"}
   :icon-wrapper {:margin "20px 16px 16px 0"}
   :icon         {:font-size "3rem"}
   :label        {:font-size "1.5rem"}

   :card-wrapper {:margin "16px 0"}
   :card-content {:text-align      "center"
                  :display         "flex"
                  :justify-content "center"
                  :align-items     "center"}
   :audio-panel  {:width "50%"}
   })

;(defn- select-method-button
;  [{:keys [on-click text icon]}]
;  (let [styles (get-styles)]
;    [ui/button {:style    (:button styles)
;                :on-click on-click}
;     [ui/typography {:variant "h3"
;                     :style   (:icon-wrapper styles)}
;      [icon {:style (:icon styles)}]]
;     [ui/typography {:variant "h5"
;                     :style   (:label styles)}
;      text]]))

;(defn- select-method-panel
;  []
;  (let [handle-record-click #(re-frame/dispatch [::add-audio.select-method/show-record-file-form])
;        handle-upload-click #(re-frame/dispatch [::add-audio.select-method/show-upload-file-form])]
;    [:div
;     [select-method-button {:text     "Upload new file"
;                            :icon     ic/cloud-upload
;                            :on-click handle-upload-click}]
;     [select-method-button {:text     "Record audio"
;                            :icon     ic/mic
;                            :on-click handle-record-click}]]))

(defn add-audio-form
  []
  (let [styles (get-styles)]
    [ui/card {:style (:card-wrapper styles)}
     [ui/card-content {:style (:card-content styles)}
      [:div {:style (:audio-panel styles)} [audio-upload-panel]]
      [:div {:style (:audio-panel styles)} [audio-record-panel]]]]))
