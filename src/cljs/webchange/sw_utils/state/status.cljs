(ns webchange.sw-utils.state.status
  (:require
    [re-frame.core :as re-frame]
    [webchange.error-message.state :as error-message]
    [webchange.logger :as logger]
    [webchange.sw-utils.message :refer [set-current-course]]
    [webchange.sw-utils.state.db :refer [path-to-db]]))

(re-frame/reg-cofx
  :online?
  (fn [co-effects]
    (assoc co-effects :online? (.-onLine js/navigator))))

;; Sync status

(def sync-statuses [:not-started
                    :disabled
                    :installing
                    :installed
                    :activating
                    :activated
                    :syncing
                    :synced
                    :offline
                    :broken])

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
  (-> (sync-status db) (= :disabled)))

(re-frame/reg-sub
  ::sync-disabled?
  sync-disabled?)

(defn sync-offline?
  [db]
  (-> (sync-status db) (= :offline)))

(re-frame/reg-sub
  ::sync-offline?
  sync-offline?)

(re-frame/reg-event-db
  ::set-sync-status
  (fn [db [_ status]]
    (if (valid-sync-status? status)
      (assoc-in db (path-to-db [:sync-status]) status)
      (do (logger/error (str "Sync status '" status "' is not valid"))
          db))))

;; Current Course

(re-frame/reg-fx
  :set-current-course
  (fn [[course-id]]
    (set-current-course course-id)))

(re-frame/reg-event-fx
  ::set-current-course
  [(re-frame/inject-cofx :online?)]
  (fn [{:keys [db online?]} [_ course-id]]
    (when-not (sync-disabled? db)
      (if online?
        {:set-current-course [course-id]}
        {:dispatch [::set-sync-status :offline]}))))

;; Last update

(re-frame/reg-sub
  ::last-update
  (fn [db]
    (get-in db (path-to-db [:last-update]))))

(re-frame/reg-event-fx
  ::set-last-update
  (fn [{:keys [db]} [_ date-str]]
    {:db (assoc-in db (path-to-db [:last-update]) date-str)}))

(re-frame/reg-sub
  ::caching-progress
  (fn [db]
    (get-in db (path-to-db [:caching-progress]))))

(re-frame/reg-event-fx
  ::set-caching-progress
  (fn [{:keys [db]} [_ progress]]
    {:db (assoc-in db (path-to-db [:caching-progress]) progress)}))

;; Version

(re-frame/reg-sub
  ::version
  (fn [db]
    (get-in db (path-to-db [:version]))))

(re-frame/reg-event-fx
  ::set-version
  (fn [{:keys [db]} [_ version]]
    {:db (assoc-in db (path-to-db [:version]) version)}))

;; Error

(re-frame/reg-event-fx
  ::handle-error
  (fn [{:keys [db]} [_ error]]
    {:db       (update-in db (path-to-db [:errors]) conj error)
     :dispatch [::error-message/show :sw-error error]}))
