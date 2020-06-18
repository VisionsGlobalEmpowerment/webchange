(ns webchange.sw-utils.state.status
  (:require
    [re-frame.core :as re-frame]
    [webchange.logger :as logger]
    [webchange.sw-utils.state.db :refer [path-to-db]]))

(def sync-statuses [:not-started
                    :disabled
                    :syncing])

(defn- valid-sync-status?
  [status]
  (some #{status} sync-statuses))

(defn sync-status
  [db]
  (get-in db (path-to-db [:sync-status])))

(re-frame/reg-sub
  ::sync-status
  sync-status)

(defn sync-disabled?
  [db]
  (-> (sync-status db)
      (= :disabled)))

(re-frame/reg-sub
  ::sync-disabled?
  sync-disabled?)

(re-frame/reg-event-db
  ::set-sync-status
  (fn [db [_ status]]

    (println "::set-sync-status" status)

    (if (valid-sync-status? status)
      (assoc-in db (path-to-db [:sync-status]) status)
      (do (logger/error (str "Sync status '" status "' is not valid"))
          db))))
