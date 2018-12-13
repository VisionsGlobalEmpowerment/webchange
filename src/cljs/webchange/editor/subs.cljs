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
  ::selected-object-action
  (fn [db]
    (get-in db [:editor :selected-object-action])))

(re-frame/reg-sub
  ::selected-scene-action
  (fn [db]
    (get-in db [:editor :selected-scene-action])))

(re-frame/reg-sub
  ::selected-scene-action
  (fn [db]
    (get-in db [:editor :selected-scene-action])))

(re-frame/reg-sub
  ::shown-scene-action
  (fn [db]
    (get-in db [:editor :shown-scene-action])))

(re-frame/reg-sub
  ::shown-form
  (fn [db]
    (get-in db [:editor :shown-form])))