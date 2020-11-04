(ns webchange.editor-v2.translator.translator-form.state.actions-shared
  (:require
    [webchange.editor-v2.translator.translator-form.state.db :refer [path-to-db]]))

(defn current-dialog-action-info
  [db]
  (get-in db (path-to-db [:current-dialog-action])))
