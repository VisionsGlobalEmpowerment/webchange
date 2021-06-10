(ns webchange.editor-v2.dialog.phrase-voice-over.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.dialog.phrase-voice-over.state :as state]
    [webchange.ui-framework.components.index :refer [dialog icon-button]]))

(defn phrase-voice-over-modal
  []
  (let [open? @(re-frame/subscribe [::state/modal-open?])
        handle-close #(re-frame/dispatch [::state/close-modal])]
    [dialog
     {:title    "Phrase Voice-over"
      :open?    open?
      :on-close handle-close}
     [:div "Phrase Voice-over Form"]]))
