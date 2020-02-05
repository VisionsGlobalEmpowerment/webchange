(ns webchange.service-worker.events
  (:require
    [re-frame.core :as re-frame]))

(re-frame/reg-event-db
  ::set-offline-mode
  (fn [db [_ mode]]
    (assoc-in db [:service-worker :offline-mode] mode)))

(re-frame/reg-event-db
  ::set-synced-game-resources
  (fn [db [_ resources]]
    (assoc-in db [:service-worker :synced-resources :game] resources)))
