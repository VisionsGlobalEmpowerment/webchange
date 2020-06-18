(ns webchange.student-dashboard.toolbar.sync.events
  (:require
    [re-frame.core :as re-frame]))

(re-frame/reg-event-fx
  ::open-sync-list
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db [:sync-resources :list-open] true)}))

(re-frame/reg-event-fx
  ::close-sync-list
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db [:sync-resources :list-open] false)}))
