(ns webchange.dashboard.events
  (:require
    [re-frame.core :as re-frame]))

(re-frame/reg-event-fx
  ::set-main-content
  (fn [{:keys [db]} [_ screen]]
    {:db (assoc-in db [:dashboard :current-main-content] screen)}))
