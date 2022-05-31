(ns webchange.admin.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]))

(def path-to-db :module/admin)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(def current-page-key :current-page)

(re-frame/reg-sub
  ::current-page
  :<- [path-to-db]
  #(get % current-page-key))

(re-frame/reg-event-fx
  ::set-current-page
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc db current-page-key value)}))
