(ns webchange.sw-utils.state.status
  (:require
    [re-frame.core :as re-frame]
    [webchange.logger :as logger]
    [webchange.sw-utils.state.db :refer [path-to-db]]))

(def sync-statuses [:not-started
                    :disabled
                    :installing
                    :installed
                    :syncing
                    :synced
                    :offline])

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

(re-frame/reg-sub
  ::last-update
  (fn [db]
    (get-in db (path-to-db [:last-update]))))

(re-frame/reg-sub
  ::version
  (fn [db]
    (get-in db (path-to-db [:version]))))

(re-frame/reg-event-db
  ::set-sync-status
  (fn [db [_ status]]
    (if (valid-sync-status? status)
      (assoc-in db (path-to-db [:sync-status]) status)
      (do (logger/error (str "Sync status '" status "' is not valid"))
          db))))

(re-frame/reg-event-db
  ::set-last-update
  (fn [db [_ date-str version]]
    (-> db
        (assoc-in (path-to-db [:last-update]) date-str)
        (assoc-in (path-to-db [:version]) version))))

(re-frame/reg-cofx
  :online?
  (fn [co-effects]
    (assoc co-effects :online? (.-onLine js/navigator))))
