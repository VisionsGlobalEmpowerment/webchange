(ns webchange.editor-v2.dialog.dialog-form.views-form
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.translator.translator-form.state.graph :as translator-form.graph]
    [webchange.editor-v2.dialog.dialog-form.views-form-concepts :refer [concepts-block]]
    [webchange.editor-v2.translator.translator-form.views-form-description :refer [description-block]]
    [webchange.editor-v2.dialog.dialog-form.views-form-diagram :refer [diagram-block]]
    [webchange.editor-v2.dialog.dialog-form.views-form-phrase :refer [node-options phrase-block]]
    [webchange.editor-v2.translator.translator-form.views-form-play-phrase :refer [play-phrase-block]]
    [webchange.editor-v2.dialog.dialog-form.views-form-target :refer [target-block text-target-block]]
    [webchange.editor-v2.text-animation-editor.views :refer [text-chunks-modal]]

    [webchange.editor-v2.dialog.dialog-form.views-audio-actions :refer [audio-actions]]
    [webchange.editor-v2.dialog.dialog-form.views-audio-warning :refer [audio-warning]]
    [webchange.editor-v2.dialog.dialog-form.views-audios-list :refer [audios-list]]
    [webchange.editor-v2.dialog.dialog-form.views-record-audio :refer [record-audio]]
    [webchange.editor-v2.dialog.dialog-form.views-upload-audio :refer [upload-audio]]
    [webchange.editor-v2.dialog.dialog-form.views-volume :refer [volume]]))

(defn- details
  [type]
  (case type
    "animation-sequence" [:div
                          [phrase-block]
                          [target-block]
                          [:div.audio-block
                           [:div
                            [volume]
                            [upload-audio]]
                           [record-audio]
                           [audio-actions]]
                          [audio-warning]
                          [audios-list]]
    "text-animation" [:div
                      ;[phrase-block]
                      [text-target-block]
                      [:div.audio-block
                       [:div
                        [volume]
                        [upload-audio]]
                       [record-audio]
                       [audio-actions]]
                      [audio-warning]
                      [audios-list]]
    nil))

(defn dialog-form
  []
  (let [current-phrase-action @(re-frame/subscribe [::translator-form.actions/current-phrase-action])
        concept-required? @(re-frame/subscribe [::translator-form.graph/concept-required])
        type (get-in current-phrase-action [:data 1 :type])]
    [:div.dialog-form
     [description-block]
     (when concept-required?
       [concepts-block])
     [diagram-block]
     [play-phrase-block]
     (if-not (nil? current-phrase-action)
       [:div
        [node-options]
        [details type]]
       [:div
        [:span "Select action on diagram"]])
     [text-chunks-modal]]))
