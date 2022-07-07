(ns webchange.lesson-builder.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]))

(def path-to-db :module/lesson-builder)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-sub
  ::current-page
  :<- [path-to-db]
  (fn [db]
    (get db :current-page)))

(re-frame/reg-event-fx
  ::set-current-page
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc db :current-page value)}))
