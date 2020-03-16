(ns webchange.student-dashboard.toolbar.sync.subs
  (:require
    [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::list-open
  (fn [db]
    (get-in db [:sync-resources :list-open])))

(re-frame/reg-sub
  ::scenes-data
  (fn [db]
    (get-in db [:sync-resources :scenes :data])))

(re-frame/reg-sub
  ::scenes-loading
  (fn [db]
    (get-in db [:sync-resources :scenes :loading])))
