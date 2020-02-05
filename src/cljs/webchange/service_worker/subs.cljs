(ns webchange.service-worker.subs
  (:require
    [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::offline-mode
  (fn [db]
    (get-in db [:service-worker :offline-mode])))

(re-frame/reg-sub
  ::get-synced-game-resources
  (fn [db]
    (get-in db [:service-worker :synced-resources :game])))
