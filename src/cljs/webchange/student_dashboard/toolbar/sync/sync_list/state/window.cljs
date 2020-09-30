(ns webchange.student-dashboard.toolbar.sync.sync-list.state.window
  (:require
    [re-frame.core :as re-frame]
    [webchange.student-dashboard.toolbar.sync.sync-list.state.db :refer [path-to-db]]
    [webchange.student-dashboard.toolbar.sync.sync-list.state.lessons-resources :as lessons-resources]
    [webchange.student-dashboard.toolbar.sync.sync-list.state.selection-state :as user-selection]
    [webchange.sw-utils.state.resources :as sw]))

(re-frame/reg-sub
  ::open?
  (fn [db]
    (get-in db (path-to-db [:open?]) false)))

(re-frame/reg-event-fx
  ::open
  (fn [{:keys [db]} [_]]
    {:db         (assoc-in db (path-to-db [:open?]) true)
     :dispatch-n (list [::lessons-resources/init]
                       [::user-selection/init])}))

(re-frame/reg-event-fx
  ::close
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db (path-to-db [:open?]) false)}))

(re-frame/reg-event-fx
  ::save
  (fn [{:keys [db]} [_]]
    (let [selected-resources (user-selection/selected-resources db)]
      {:db         (assoc-in db (path-to-db [:open?]) false)
       :dispatch-n (list [::close]
                         [::sw/cache-resources selected-resources])})))
