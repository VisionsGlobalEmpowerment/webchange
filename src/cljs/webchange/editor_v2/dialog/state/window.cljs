(ns webchange.editor-v2.dialog.state.window
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.state.db :refer [path-to-db]]
    [webchange.editor-v2.translator.translator-form.state.form :as translator-form]
    [webchange.editor-v2.translator.translator-form.state.window-confirmation]
    [webchange.editor-v2.history.state :as history]))

;; Subs

(re-frame/reg-sub
  ::modal-state
  (fn [db]
    (-> db
        (get-in (path-to-db [:translator-dialog-modal-state]))
        boolean)))

;; Events

(re-frame/reg-event-fx
  ::open
  (fn [{:keys [db]} [_ props]]
    {:db         (assoc-in db (path-to-db [:translator-dialog-modal-state]) true)
     :dispatch-n (list [::translator-form/init-state props]
                       [::history/init-history])}))

(re-frame/reg-event-fx
  ::close
  (fn [{:keys [db]} [_]]
    {:db                 (assoc-in db (path-to-db [:translator-dialog-modal-state]) false)
     :dispatch-n         (list [::translator-form/reset-state])
     :reset-before-leave true}))
