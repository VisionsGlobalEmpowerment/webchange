(ns webchange.dashboard.courses.subs
  (:require
    [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::courses-list
  (fn [db]
    (get-in db [:dashboard :courses])))

(re-frame/reg-sub
  ::courses-loading
  (fn [db]
    (get-in db [:loading :courses])))
