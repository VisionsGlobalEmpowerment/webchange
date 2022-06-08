(ns webchange.admin.pages.activity-edit.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :page/activity-edit)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; Activity Loading

(def activity-loading-key :activity-loading?)

(defn- set-activity-loading
  [db value]
  (assoc db activity-loading-key value))

(re-frame/reg-sub
  ::activity-loading?
  :<- [path-to-db]
  #(get % activity-loading-key false))

;; Activity Data

(def activity-key :activity)

(defn- get-activity
  [db]
  (get db activity-key))

(defn- set-activity
  [db value]
  (assoc db activity-key value))

(re-frame/reg-sub
  ::activity
  :<- [path-to-db]
  #(get-activity %))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [activity-id]}]]
    {:db       (set-activity-loading db true)
     :dispatch [::warehouse/load-available-activity
                {:activity-id activity-id}
                {:on-success [::load-activity-success]}]}))

(re-frame/reg-event-fx
  ::load-activity-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data]]
    {:db (-> db
             (set-activity-loading false)
             (set-activity data))}))

(re-frame/reg-event-fx
  ::edit
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [id]} (get-activity db)]
      (print "::edit" id)
      {})))

(re-frame/reg-event-fx
  ::play
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [id]} (get-activity db)]
      (print "::play" id)
      {})))
