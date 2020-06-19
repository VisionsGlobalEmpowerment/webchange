(ns webchange.sw-utils.state.resources
  (:require
    [re-frame.core :as re-frame]
    [webchange.sw-utils.state.status :as status]
    [webchange.sw-utils.message :as sw]
    [webchange.sw-utils.state.db :refer [path-to-db]]))

(re-frame/reg-fx
  :cache-course
  (fn [[course-id]]
    (sw/cache-course course-id)))

(re-frame/reg-event-fx
  ::cache-course
  [(re-frame/inject-cofx :online?)]
  (fn [{:keys [db online?]} [_ course-id]]
    (when-not (status/sync-disabled? db)
      (if online?
        {:cache-course [course-id]
         :dispatch     [::status/set-sync-status :syncing]}
        {:dispatch [::status/set-sync-status :offline]}))))

(re-frame/reg-sub
  ::scenes-data
  (fn [db]
    (get-in db [:sync-resources :scenes :data])))

(re-frame/reg-sub
  ::scenes-loading
  (fn [db]
    (get-in db [:sync-resources :scenes :loading])))

(re-frame/reg-sub
  ::synced-game-resources
  (fn [db]
    (get-in db [:service-worker :synced-resources :game])))

(re-frame/reg-event-db
  ::set-synced-game-resources
  (fn [db [_ resources]]
    (assoc-in db (path-to-db [:synced-resources :game]) resources)))