(ns webchange.editor-v2.question.state.window
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.state.db :refer [path-to-db]]
    [webchange.editor-v2.translator.translator-form.state.form :as translator-form]
    [webchange.editor-v2.question.question-form.state.form :as question-form]
    [webchange.editor-v2.translator.translator-form.state.window-confirmation]
    [webchange.editor-v2.history.state :as history]))

;; Subs

(re-frame/reg-sub
  ::modal-state
  (fn [db]
    (-> db
        (get-in (path-to-db [:question-editor-modal-state]))
        boolean)))

;; Events

(re-frame/reg-event-fx
  ::open
  (fn [{:keys [db]} [_]]
    {:db         (assoc-in db (path-to-db [:question-editor-modal-state]) true)
     :dispatch-n (list [::question-form/init-state]
                       [::history/init-history])}))

(re-frame/reg-event-fx
  ::close
  (fn [{:keys [db]} [_]]
    {:db                 (assoc-in db (path-to-db [:question-editor-modal-state]) false)
     :dispatch-n         (list [::translator-form/reset-state])
     :reset-before-leave true}))
