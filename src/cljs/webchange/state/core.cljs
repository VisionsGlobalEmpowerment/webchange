(ns webchange.state.core
  (:require
    [re-frame.core :as re-frame]))

(defn current-course-id
  [db]
  (get db :current-course))

(defn current-scene-id
  [db]
  (get db :current-scene))

(defn get-scene-data
  [db scene-id]
  (get-in db [:scenes scene-id]))

(re-frame/reg-event-fx
  ::set-scene-data
  (fn [{:keys [db]} [_ {:keys [scene-id scene-data]}]]
    {:db (assoc-in db [:scenes scene-id] scene-data)}))
