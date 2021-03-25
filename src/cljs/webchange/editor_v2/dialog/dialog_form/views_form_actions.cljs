(ns webchange.editor-v2.dialog.dialog-form.views-form-actions
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.state.form :as translator-form.form]
    [webchange.editor-v2.dialog.state.window :as dialog-state-window]
    [webchange.ui-framework.components.index :refer [button]]))

(defn form-actions
  []
  (let [handle-save #(do
                       (re-frame/dispatch [::translator-form.form/save-changes])
                       (re-frame/dispatch [::dialog-state-window/close]))]
    [:div.form-actions
     [button {:size     "big"
              :on-click handle-save}
      "Save"]]))
