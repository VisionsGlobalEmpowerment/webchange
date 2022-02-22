(ns webchange.editor-v2.question.question-form.state.actions
  (:require
    [ajax.core :refer [json-request-format json-response-format]]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.question.question-form.state.db :refer [path-to-db]]
    [webchange.editor-v2.question.question-form.state.actions-utils :as actions]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]))


(defn current-question-action-info
  [db]
  (get-in db (path-to-db [:current-question-action])))

;;; Subs

(re-frame/reg-sub
  ::current-question-action-info
  current-question-action-info)

(re-frame/reg-sub
  ::current-question-text-action
  (fn []
    [(re-frame/subscribe [::current-question-action-info])
     (re-frame/subscribe [::translator-form.scene/actions-data])])
  (fn [[info actions]]
    (get-in actions (drop-last 2 (:path info)))))

(re-frame/reg-sub
  ::is-question-dialog-action
  (fn [] [(re-frame/subscribe [::current-question-action-info])])
  (fn [[info]]
    (not (= (first (:path info)) (first (:question-path info))))))


;; Dialog Action

(re-frame/reg-event-fx
  ::set-current-question-action
  (fn [{:keys [db]} [_ action-node]]
    (let [action-info (if-not (nil? action-node)
                        (actions/node->info action-node)
                        nil)]
      {:dispatch-n (list [::translator-form.actions/set-current-dialog-action action-info]
                         [::translator-form.actions/set-current-phrase-action action-info])
       :db         (assoc-in db (path-to-db [:current-question-action]) action-info)})))

(re-frame/reg-event-fx
  ::set-current-question-text-action
  (fn [{:keys [db]} [_ text]]
    (let [question-action (current-question-action-info db)
          action-path (drop-last 2 (:path question-action))]
      {:dispatch-n (list [::translator-form.scene/update-action action-path {:text text}])})))
