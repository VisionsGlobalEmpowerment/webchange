(ns webchange.dashboard.schools.subs
  (:require
    [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::schools-list
  (fn [db]
    (get-in db [:dashboard :schools])))

(re-frame/reg-sub
  ::current-school-id
  (fn [db]
    (get-in db [:dashboard :current-school-id])))

(re-frame/reg-sub
  ::current-school
  (fn [db]
    (let [current-school-id (get-in db [:dashboard :current-school-id])]
      (->> (get-in db [:dashboard :schools])
           (filter #(= current-school-id (:id %)))
           first))))

(re-frame/reg-sub
  ::school-modal-state
  (fn [db]
    (get-in db [:dashboard :school-modal-state])))

(re-frame/reg-sub
  ::schools-loading
  (fn [db]
    (get-in db [:loading :schools])))

(re-frame/reg-sub
  ::delete-modal-state
  (fn [db]
    (get-in db [:dashboard :delete-school-modal-state])))