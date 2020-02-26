(ns webchange.service-worker.events
  (:require
    [re-frame.core :as re-frame]))

(re-frame/reg-event-db
  ::set-offline-mode
  (fn [db [_ mode]]
    (assoc-in db [:service-worker :offline-mode] mode)))

(re-frame/reg-event-db
  ::set-last-update
  (fn [db [_ date-str version]]
    (-> db
        (assoc-in [:service-worker :last-update] date-str)
        (assoc-in [:service-worker :version] version))))

(re-frame/reg-event-db
  ::set-synced-game-resources
  (fn [db [_ resources]]
    (assoc-in db [:service-worker :synced-resources :game] resources)))
