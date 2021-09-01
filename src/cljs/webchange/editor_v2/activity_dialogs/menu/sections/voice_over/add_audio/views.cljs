(ns webchange.editor-v2.activity-dialogs.menu.sections.voice-over.add-audio.views
  (:require
    [reagent.core :as r]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.dialog.components.record-audio.views :refer [record-audio]]
    [webchange.editor-v2.dialog.dialog-form.views-upload-audio :refer [upload-audio]]
    [webchange.ui-framework.components.index :refer [select]]
    [webchange.editor-v2.translator.translator-form.state.audios :as state]))

(def lang-options [{:text  "English"
                    :value "english"}
                   {:text  "Spanish"
                    :value "spanish"}])

(defn add-audio
  []
  (r/with-let [show-upload-form? (r/atom true)
               handle-start-record #(reset! show-upload-form? false)
               handle-stop-record #(reset! show-upload-form? true)
               handle-lang-change #(re-frame/dispatch [::state/set-current-lang %])]
    (let [current-lang @(re-frame/subscribe [::state/current-lang])]
      [:div.add-audio
       [select {:value       current-lang
                :options     lang-options
                :on-change   handle-lang-change
                :variant     "outlined"
                :placeholder "Select Language"}]
       (into [:div.add-audio-form
              [:div.input-label.thin "Record voice"]
              [record-audio {:on-start-record handle-start-record
                             :on-stop-record  handle-stop-record}]]
             (when @show-upload-form?
               [[:div.input-label.thin "or"]
                [upload-audio {:input-props {:show-icon?   false
                                             :show-input?  false
                                             :button-text  "Upload file"
                                             :button-props {:color   "default"
                                                            :variant "outlined"}}}]]))])))
