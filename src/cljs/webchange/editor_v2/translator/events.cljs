(ns webchange.editor-v2.translator.events
  (:require
    [re-frame.core :as re-frame]
    [ajax.core :refer [json-request-format json-response-format]]))

(re-frame/reg-event-fx
  ::open-translator-modal
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db [:editor-v2 :translator :translator-modal-state] true)}))

(re-frame/reg-event-fx
  ::close-translator-modal
  (fn [{:keys [db]} [_]]
    {:db (-> db
             (assoc-in [:editor-v2 :translator :translator-modal-state] false)
             (assoc-in [:editor-v2 :translator :phrase-translation-data] {})
             (assoc-in [:editor-v2 :translator :selected-action] nil)
             (assoc-in [:editor-v2 :translator :current-concept] nil))}))

(re-frame/reg-event-fx
  ::set-blocking-progress
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db [:editor-v2 :translator :blocking-progress] value)}))

(re-frame/reg-event-fx
  ::set-current-selected-action
  (fn [{:keys [db]} [_ action]]
    {:db (assoc-in db [:editor-v2 :translator :selected-action] action)}))

(re-frame/reg-event-fx
  ::clean-current-selected-action
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db [:editor-v2 :translator :selected-action] nil)}))

(re-frame/reg-event-fx
  ::set-phrase-translation-action
  (fn [{:keys [db]} [_ action-name action-id data]]
    {:db (assoc-in db [:editor-v2 :translator :phrase-translation-data [action-name action-id]] data)}))

(re-frame/reg-event-fx
  ::set-current-concept
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db [:editor-v2 :translator :current-concept] data)}))
