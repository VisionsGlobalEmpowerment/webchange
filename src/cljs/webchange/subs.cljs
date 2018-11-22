(ns webchange.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::name
  (fn [db]
    (:name db)))

(re-frame/reg-sub
  ::viewport
  (fn [db]
    (:viewport db)))

(re-frame/reg-sub
  ::current-scene
  (fn [db]
    (:current-scene db)))

(re-frame/reg-sub
  ::scene
  (fn [db [_ scene-id]]
    (get-in db [:scenes scene-id] {})))

(re-frame/reg-sub
  ::scene-objects
  (fn [db [_ scene-id]]
    (get-in db [:scenes scene-id :scene-objects] [])))

(re-frame/reg-sub
  ::scene-object
  (fn [db [_ scene-id name]]
    (get-in db [:scenes scene-id :objects (keyword name)] {})))

(re-frame/reg-sub
  ::scene-loading-progress
  (fn [db [_ scene-id]]
    (get-in db [:scene-loading-progress scene-id] 0)))

(re-frame/reg-sub
  ::scene-loading-complete
  (fn [db [_ scene-id]]
    (get-in db [:scene-loading-complete scene-id] false)))

(re-frame/reg-sub
  ::playing
  (fn [db]
    (:playing db)))

(re-frame/reg-sub
  ::scene-started
  (fn [db]
    (:scene-started db)))

(re-frame/reg-sub
  ::ui-screen
  (fn [db]
    (:ui-screen db)))

(re-frame/reg-sub
  ::get-music-volume
  (fn [db]
    (:music-volume db)))

(re-frame/reg-sub
  ::get-effects-volume
  (fn [db]
    (:effects-volume db)))

(re-frame/reg-sub
  ::placeholder-item
  (fn [db [_ scene-id item-id]]
    (get-in db [:scenes scene-id :placeholders item-id])))