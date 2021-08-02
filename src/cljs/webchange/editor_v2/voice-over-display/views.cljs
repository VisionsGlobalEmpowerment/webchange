(ns webchange.editor-v2.voice-over-display.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.ui-framework.components.index :refer [button dialog]]
    [webchange.editor-v2.voice-over-display.state :as state]))

(defn voice-over-display-modal
  []
  (let [open? @(re-frame/subscribe [::state/modal-state])
        cancel #(re-frame/dispatch [::state/cancel])]
    (when open?
      [dialog
       {:title    ""
        :on-close cancel
        :actions  [[button {:on-click cancel
                            :variant  "outlined"
                            :size     "big"}
                    "Cancel"]]}])))
