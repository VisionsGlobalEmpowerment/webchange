(ns webchange.editor-v2.dialog.dialog-form.views-form-actions
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.state.form :as translator-form.form]
    [webchange.ui-framework.components.index :refer [button]]))

(defn form-actions
  []
  (let [handle-save #(re-frame/dispatch [::translator-form.form/save-changes])]
    [:div.form-actions
     [button {:size     "big"
              :on-click handle-save}
      "Save"]]))
