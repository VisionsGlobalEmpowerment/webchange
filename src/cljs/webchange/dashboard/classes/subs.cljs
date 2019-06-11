(ns webchange.dashboard.classes.subs
  (:require
    [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::classes-list
  (fn [db]
    (get-in db [:dashboard :classes])))

(re-frame/reg-sub
  ::class
  (fn [db [_ id]]
    (->> (get-in db [:dashboard :classes])
         (filter #(= id (:id %)))
         first)))

(re-frame/reg-sub
  ::current-class-id
  (fn [db]
    (get-in db [:dashboard :current-class-id])))
