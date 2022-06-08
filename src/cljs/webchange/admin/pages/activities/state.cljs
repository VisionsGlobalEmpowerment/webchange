(ns webchange.admin.pages.activities.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :page/activities)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; Activities Loading

(def activities-loading-key :activities-loading?)

(defn- set-activities-loading
  [db value]
  (assoc db activities-loading-key value))

(re-frame/reg-sub
  ::activities-loading?
  :<- [path-to-db]
  #(get % activities-loading-key false))

;; Activities Data

(def activities-key :activities)

(defn- get-activities
  [db]
  (get db activities-key))

(defn- set-activities
  [db value]
  (assoc db activities-key value))

(re-frame/reg-sub
  ::activities
  :<- [path-to-db]
  #(get-activities %))

;;

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db       (set-activities-loading db true)
     :dispatch [::warehouse/load-available-activities
                {:on-success [::load-activities-success]}]}))

(re-frame/reg-event-fx
  ::load-activities-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data]]
    {:db (-> db
             (set-activities-loading false)
             (set-activities data))}))

(re-frame/reg-event-fx
  ::open-activity
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ activity-id]]
    (print "::open-activity")
    {}))

(re-frame/reg-event-fx
  ::edit-activity
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_ activity-id]]
    (print "::edit-activity" activity-id)
    {:dispatch [::routes/redirect :activity-edit :activity-id activity-id]}))
