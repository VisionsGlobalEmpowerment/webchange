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

(re-frame/reg-sub
  ::current-class
  (fn [db]
    (let [current-class-id (get-in db [:dashboard :current-class-id])]
      (->> (get-in db [:dashboard :classes])
           (filter #(= current-class-id (:id %)))
           first))))

(re-frame/reg-sub
  ::class-modal-state
  (fn [db]
    (get-in db [:dashboard :class-modal-state])))

(re-frame/reg-sub
  ::classes-loading
  (fn [db]
    (get-in db [:loading :classes])))

(re-frame/reg-sub
  ::class-profile-loading
  (fn [db]
    (get-in db [:loading :class-profile])))

(re-frame/reg-sub
  ::class-profile
  (fn [db]
    (get-in db [:dashboard :class-profile])))

(re-frame/reg-sub
  ::delete-modal-state
  (fn [db]
    (get-in db [:dashboard :delete-class-modal-state])))