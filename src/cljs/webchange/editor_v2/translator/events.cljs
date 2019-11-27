(ns webchange.editor-v2.translator.events
  (:require
    [re-frame.core :as re-frame]))

(re-frame/reg-event-fx
  ::open-translator-modal
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db [:editor-v2 :translator :translator-modal-state] true)}))

(re-frame/reg-event-fx
  ::close-translator-modal
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db [:editor-v2 :translator :translator-modal-state] false)}))

(re-frame/reg-event-fx
  ::set-current-selected-action
  (fn [{:keys [db]} [_ action]]
    {:db (assoc-in db [:editor-v2 :translator :selected-action] action)}))

(re-frame/reg-event-fx
  ::clean-current-selected-action
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db [:editor-v2 :translator :selected-action] nil)}))
