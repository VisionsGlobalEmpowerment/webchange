(ns webchange.dashboard.courses.subs
  (:require
    [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::books-list
  (fn [db]
    (get-in db [:dashboard :courses :books])))

(re-frame/reg-sub
  ::courses-list
  (fn [db]
    (get-in db [:dashboard :courses :courses])))

(re-frame/reg-sub
  ::published-books-list
  (fn [db]
    (get-in db [:dashboard :courses :published-books])))

(re-frame/reg-sub
  ::published-courses-list
  (fn [db]
    (get-in db [:dashboard :courses :published-courses])))

(re-frame/reg-sub
  ::courses-loading
  (fn [db]
    (or
      (get-in db [:loading :courses])
      (get-in db [:loading :books])
      (get-in db [:loading :published-courses])
      (get-in db [:loading :published-books]))))
