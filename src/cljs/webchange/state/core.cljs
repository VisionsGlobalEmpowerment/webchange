(ns webchange.state.core
  (:require
    [re-frame.core :as re-frame]))

(defn current-course-id
  [db]
  (get db :current-course))

(defn current-scene-id
  [db]
  (get db :current-scene))

(re-frame/reg-sub
  ::current-scene-id
  current-scene-id)

(defn scenes-data
  [db]
  (get db :scenes))

(re-frame/reg-sub
  ::scenes-data
  scenes-data)

(defn get-scene-data
  [db scene-id]
  (get-in db [:scenes scene-id]))

(re-frame/reg-event-fx
  ::set-scene-data
  (fn [{:keys [db]} [_ {:keys [scene-id scene-data]}]]
    {:db (assoc-in db [:scenes scene-id] scene-data)}))
