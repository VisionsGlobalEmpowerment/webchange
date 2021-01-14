(ns webchange.editor-v2.question.question-form.state.audios
  (:require
    [ajax.core :refer [json-response-format]]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]
    [webchange.editor-v2.translator.translator-form.state.audios-utils :refer [get-audio-assets-data
                                                                               get-form-data]]))

;; Subs

(re-frame/reg-sub
  ::audios-list
  (fn []
    [(re-frame/subscribe [::translator-form.actions/current-phrase-action])
     (re-frame/subscribe [::translator-form.scene/audio-assets])])
  (fn [[current-phrase-action scene-audios]]
    (let [question-or-answer? (nil? (get-in current-phrase-action [:type]))]
      (if question-or-answer?
        (get-audio-assets-data current-phrase-action scene-audios)
        (get-audio-assets-data (get-in current-phrase-action [:data 1]) scene-audios)))))
