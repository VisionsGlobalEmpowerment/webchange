(ns webchange.dashboard.subs
  (:require
    [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::current-main-content
  (fn [db]
    (get-in db [:dashboard :current-main-content])))

(re-frame/reg-sub
  ::classes
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

(re-frame/reg-sub
  ::class-students
  (fn [db [_ id]]
    (get-in db [:dashboard :students id])))

(re-frame/reg-sub
  ::current-student
  (fn [db]
    (get-in db [:dashboard :current-student])))