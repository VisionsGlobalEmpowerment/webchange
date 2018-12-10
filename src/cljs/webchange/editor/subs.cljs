(ns webchange.editor.subs
  (:require
    [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::screen
  (fn [db]
    (get-in db [:editor :screen])))

(re-frame/reg-sub
  ::transform
  (fn [db]
    (get-in db [:editor :transform])))

(re-frame/reg-sub
  ::action
  (fn [db]
    (get-in db [:editor :action])))