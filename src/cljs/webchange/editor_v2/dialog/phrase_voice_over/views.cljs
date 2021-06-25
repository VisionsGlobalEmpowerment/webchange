(ns webchange.editor-v2.dialog.phrase-voice-over.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.components.audio-wave-form.views :refer [audio-wave-form]]
    [webchange.editor-v2.dialog.phrase-voice-over.state :as state]
    [webchange.ui-framework.components.index :refer [dialog]]))

(defn- phrase-voice-over-form
  []
  (let [{:keys [url] :as audio-data} @(re-frame/subscribe [::state/audio-data])
        handle-change-region #(re-frame/dispatch [::state/update-audio-region %])]
    [:div
     (when (some? url)
       [audio-wave-form (merge audio-data
                               {:show-controls? true
                                :on-change      handle-change-region})])]))

(defn phrase-voice-over-modal
  []
  (let [open? @(re-frame/subscribe [::state/modal-open?])
        handle-close #(re-frame/dispatch [::state/close-modal])]
    [dialog
     {:title    "Select Audio Form"
      :open?    open?
      :on-close handle-close}
     [phrase-voice-over-form]]))
