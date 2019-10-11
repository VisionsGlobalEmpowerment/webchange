(ns webchange.dashboard.students.subs
  (:require
    [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::class-students
  (fn [db [_ id]]
    (get-in db [:dashboard :students id])))

(re-frame/reg-sub
  ::unassigned-students
  (fn [db [_ id]]
    (get-in db [:dashboard :unassigned-students])))

(re-frame/reg-sub
  ::current-student-id
  (fn [db]
    (get-in db [:dashboard :current-student-id])))

(re-frame/reg-sub
  ::current-student
  (fn [db]
    (get-in db [:dashboard :current-student])))

(re-frame/reg-sub
  ::generated-code
  (fn [db]
    (get-in db [:dashboard :access-code])))

(re-frame/reg-sub
  ::student-modal-state
  (fn [db]
    (get-in db [:dashboard :student-modal-state])))

(re-frame/reg-sub
  ::students-loading
  (fn [db]
    (get-in db [:loading :students])))

(re-frame/reg-sub
  ::student-loading
  (fn [db]
    (get-in db [:loading :student])))

(re-frame/reg-sub
  ::student-profile-loading
  (fn [db]
    (get-in db [:loading :student-profile])))

(re-frame/reg-sub
  ::student-profile
  (fn [db]
    (get-in db [:dashboard :student-profile])))

(re-frame/reg-sub
  ::remove-from-class-modal-state
  (fn [db]
    (get-in db [:dashboard :remove-student-from-class-modal-state])))

(re-frame/reg-sub
  ::delete-modal-state
  (fn [db]
    (get-in db [:dashboard :delete-student-modal-state])))

(re-frame/reg-sub
  ::complete-modal-state
  (fn [db]
    (get-in db [:dashboard :complete-student-modal-state])))