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
  (fn [{:keys [db]} [_ course-id]]
    (when-not (status/sync-disabled? db)
      {:cache-course [course-id]
       :dispatch     [::status/set-sync-status :syncing]})))
