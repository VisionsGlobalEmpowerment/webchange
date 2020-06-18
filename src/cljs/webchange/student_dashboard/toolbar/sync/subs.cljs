(ns webchange.student-dashboard.toolbar.sync.subs
  (:require
    [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::list-open
  (fn [db]
    (get-in db [:sync-resources :list-open])))
