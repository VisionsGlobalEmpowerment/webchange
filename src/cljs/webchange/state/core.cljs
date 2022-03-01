(ns webchange.state.core
  (:require
    [re-frame.core :as re-frame]))

(defn current-course-id
  [db]
  (get db :current-course))

(re-frame/reg-sub
  ::current-course-id
  current-course-id)

;; Current scene id

(def current-scene-id-path [:current-scene])

(defn current-scene-id
  [db]
  (get-in db current-scene-id-path))

(re-frame/reg-sub
  ::current-scene-id
  current-scene-id)

(re-frame/reg-event-fx
  ::set-current-scene-id
  (fn [{:keys [db]} [_ scene-id]]
    {:db (assoc-in db current-scene-id-path scene-id)}))

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

(re-frame/reg-event-fx
  ::update-scene-data
  (fn [{:keys [db]} [_ {:keys [scene-id data-path data-patch]}]]
    {:db (update-in db (concat [:scenes scene-id] data-path) merge data-patch)}))
