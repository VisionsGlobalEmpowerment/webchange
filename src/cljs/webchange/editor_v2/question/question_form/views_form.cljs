(ns webchange.editor-v2.question.question-form.views-form
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.dialog.dialog-form.views-form-phrase :as dialog-form.phrase]
    [webchange.editor-v2.dialog.dialog-form.audio-assets.views :as dialog-form.views]
    [webchange.editor-v2.question.question-form.state.actions :as question-form.actions]
    [webchange.editor-v2.question.question-form.audio-assets.views :refer [audios-block]]
    [webchange.editor-v2.question.question-form.views-form-description :refer [text-block]]
    [webchange.editor-v2.question.question-form.views-form-diagram :refer [diagram-block]]
    [webchange.editor-v2.question.text.views-text-animation-editor :refer [text-chunks-modal]]
    [webchange.ui.theme :refer [get-in-theme]]))

(defn question-form
  []
  (let [question-dialog-action? @(re-frame/subscribe [::question-form.actions/is-question-dialog-action])]
    [:div
     [diagram-block]
     (if question-dialog-action?
       [:div
        [dialog-form.phrase/phrase-block]
        [dialog-form.views/audios-block]]
       [:div [:div [audios-block]]
        [text-block]])
     [text-chunks-modal]]))
