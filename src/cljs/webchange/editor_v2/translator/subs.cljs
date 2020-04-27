(ns webchange.editor-v2.translator.subs
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.db :refer [path-to-db]]))

(re-frame/reg-sub
  ::translator-modal-state
  (fn [db]
    (-> db
        (get-in (path-to-db [:translator-modal-state]))
        boolean)))
