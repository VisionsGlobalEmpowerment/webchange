(ns webchange.editor-v2.question.question-form.state.actions
  (:require
    [ajax.core :refer [json-request-format json-response-format]]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.text.core :refer [parts->chunks]]
    [webchange.editor-v2.question.question-form.state.db :refer [path-to-db]]
    [webchange.editor-v2.translator.translator-form.state.actions-shared :as actions-shared]
    [webchange.editor-v2.question.question-form.state.actions-utils :as actions]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    ;[webchange.editor-v2.translator.translator-form.state.concepts :as translator-form.concepts]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]
    [webchange.subs :as subs]
    ))


(defn current-question-action-info
  [db]
  (get-in db (path-to-db [:current-question-action])))

;;; Subs
;
;(def current-dialog-action-info actions-shared/current-dialog-action-info)
;
(re-frame/reg-sub
  ::current-question-action-info
  current-question-action-info)


(re-frame/reg-sub
  ::current-question-text-action
  (fn []
    [(re-frame/subscribe [::current-question-action-info])
     (re-frame/subscribe [::translator-form.scene/actions-data])
     ])
  (fn [[info actions]]
        (get-in actions (actions/node-path->action-path (drop-last (:path info))))))

(re-frame/reg-sub
  ::is-question-dialog-action
  (fn [] [(re-frame/subscribe [::current-question-action-info])])
  (fn [[info]]
    (not (= (first (:path info)) (first (:question-path info))))))


;; Dialog Action

(re-frame/reg-event-fx
  ::set-current-question-action
  (fn [{:keys [db]} [_ action-node]]
    (println "::set-current-question-action" action-node)
    (let [action-info (if-not (nil? action-node)
                        (actions/node->info action-node)
                        nil)]
      {:dispatch-n (list [::translator-form.actions/set-current-dialog-action action-info]
                         [::translator-form.actions/set-current-phrase-action action-info])
       :db (assoc-in db (path-to-db [:current-question-action]) action-info)})))

(re-frame/reg-event-fx
  ::set-current-question-text-action
  (fn [{:keys [db]} [_ text]]
    (let [question-action (current-question-action-info db)
          path (drop-last (:path question-action))
          action-path (actions/node-path->action-path path)]
      {:dispatch-n (list [::translator-form.scene/update-action action-path {:text text}])})))
