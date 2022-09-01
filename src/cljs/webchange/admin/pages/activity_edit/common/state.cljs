(ns webchange.admin.pages.activity-edit.common.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :page/activity-edit--common)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; activity loading

(def activity-loading-key :activity-loading?)

(defn- set-activity-loading
  [db value]
  (assoc db activity-loading-key value))

(re-frame/reg-sub
  ::activity-loading?
  :<- [path-to-db]
  #(get % activity-loading-key false))

;; activity data

(def activity-key :activity-data)

(defn- get-activity
  [db]
  (get db activity-key))

(defn- set-activity
  [db value]
  (assoc db activity-key value))

(defn- update-activity
  [db data-patch]
  (let [activity-data (get-activity db)
        updated-data (-> (merge activity-data data-patch)
                         (assoc :metadata (merge (:metadata activity-data)
                                                 (:metadata data-patch))))]
    (assoc db activity-key updated-data)))

(re-frame/reg-sub
  ::activity-data
  :<- [path-to-db]
  #(get-activity %))

(re-frame/reg-event-fx
  ::update-activity-data
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data-patch]]
    {:db (update-activity db data-patch)}))

;;

(re-frame/reg-sub
  ::activity-id
  :<- [::activity-data]
  #(get % :id))

(re-frame/reg-sub
  ::activity-locked?
  :<- [::activity-data]
  #(get-in % [:metadata :locked] false))

(re-frame/reg-sub
  ::activity-published?
  :<- [::activity-data]
  #(= "visible" (:status %)))

;; load activity

(re-frame/reg-event-fx
  ::load-activity
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ activity-id]]
    {:db       (set-activity-loading db true)
     :dispatch [::warehouse/load-activity
                {:activity-id activity-id}
                {:on-success [::load-activity-success]
                 :on-failure [::load-activity-failure]}]}))

(re-frame/reg-event-fx
  ::load-activity-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data]]
    {:db (-> db
             (set-activity-loading false)
             (set-activity data))}))

(re-frame/reg-event-fx
  ::load-activity-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db (set-activity-loading false))}))


;; init

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_ {:keys [activity-id]}]]
    {:dispatch [::load-activity activity-id]}))
