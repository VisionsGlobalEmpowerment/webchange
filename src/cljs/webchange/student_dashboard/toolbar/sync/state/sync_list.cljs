(ns webchange.student-dashboard.toolbar.sync.state.sync-list
  (:require
    [re-frame.core :as re-frame]
    [webchange.student-dashboard.toolbar.sync.state.db :as db]
    [webchange.student-dashboard.toolbar.sync.state.course-resources :as course-resources]))

(defn- path-to-db
  [relative-path]
  (->> relative-path
       (concat [:window])
       (db/path-to-db)))

(re-frame/reg-sub
  ::open?
  (fn [db]
    (get-in db (path-to-db [:open?]) false)))

(re-frame/reg-event-fx
  ::open
  (fn [{:keys [db]} [_]]
    {:db       (assoc-in db (path-to-db [:open?]) true)
     :dispatch [::course-resources/init-state]}))

(re-frame/reg-event-fx
  ::cancel
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db (path-to-db [:open?]) false)}))

(re-frame/reg-event-fx
  ::save
  (fn [{:keys [db]} [_]]
    {:db       (assoc-in db (path-to-db [:open?]) false)
     :dispatch [::course-resources/save-sync-list]}))
