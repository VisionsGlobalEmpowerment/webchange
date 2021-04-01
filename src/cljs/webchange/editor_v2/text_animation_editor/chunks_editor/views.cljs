(ns webchange.editor-v2.text-animation-editor.chunks-editor.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.text-animation-editor.chunks-editor.state :as state]
    [webchange.editor-v2.text-animation-editor.chunks-editor.views-form :refer [chunks-editor-form]]
    [webchange.ui-framework.components.index :refer [button dialog]]))

(defn- configuration-form-container
  []
  (let [text-data @(re-frame/subscribe [::state/current-dialog-text])]
    [chunks-editor-form (merge (select-keys text-data [:text :chunks])
                               {:on-change (fn [data-patch]
                                             (re-frame/dispatch [::state/update-text-data data-patch]))})]))

(defn configuration-modal
  []
  (let [open? @(re-frame/subscribe [::state/modal-state])
        save #(re-frame/dispatch [::state/save])
        close #(re-frame/dispatch [::state/close])]
    (when open?
      [dialog
       {:title    "Edit text chunks"
        :on-close close
        :actions  [[button {:on-click save
                            :size     "big"}
                    "Save"]
                   [button {:on-click close
                            :variant  "outlined"
                            :size     "big"}
                    "Cancel"]]}
       [configuration-form-container]])))
