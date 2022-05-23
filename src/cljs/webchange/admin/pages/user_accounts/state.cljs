(ns webchange.admin.pages.user-accounts.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :page/user-accounts)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; Flags

(def loading-key :loading?)

(defn- set-loading
  [db value]
  (assoc db loading-key value))

(re-frame/reg-sub
  ::loading?
  :<- [path-to-db]
  #(get % loading-key false))

;; Users

(def users-key :users)

(defn- set-users
  [db data]
  (assoc db users-key data))

(re-frame/reg-sub
  ::users
  :<- [path-to-db]
  (fn [db]
    (->> (get db users-key [])
         (map (fn [{:keys [first-name last-name] :as user}]
                (merge user
                       {:name (str first-name " " last-name)}))))))

;;

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db       (-> db (set-loading true))
     :dispatch [::warehouse/load-users
                {:on-success [::load-users-success]
                 :on-failure [::load-users-failure]}]}))

(re-frame/reg-event-fx
  ::load-users-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ users]]
    {:db (-> db
             (set-loading false)
             (set-users users))}))

(re-frame/reg-event-fx
  ::load-users-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db (set-loading false))}))

(re-frame/reg-event-fx
  ::edit-user
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ user-id]]
    {:dispatch [::routes/redirect :user-profile :user-id user-id]}))
