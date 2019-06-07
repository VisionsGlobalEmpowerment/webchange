(ns webchange.dashboard.subs
  (:require
    [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::current-main-content
  (fn [db]
    (get-in db [:dashboard :current-main-content])))
