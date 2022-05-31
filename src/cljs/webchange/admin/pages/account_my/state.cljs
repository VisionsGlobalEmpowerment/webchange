(ns webchange.admin.pages.account-my.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :page/my-account)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; Account Loading

(def account-loading-key :account-loading?)

(defn- set-account-loading
  [db value]
  (assoc db account-loading-key value))

(re-frame/reg-sub
  ::account-loading?
  :<- [path-to-db]
  #(get % account-loading-key false))

;; Account Data

(def account-data-key :account-data)

(defn- get-account-data
  [db]
  (get db account-data-key))

(defn- set-account-data
  [db value]
  (assoc db account-data-key value))

(re-frame/reg-sub
  ::account-data
  :<- [path-to-db]
  #(get-account-data %))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db       (set-account-loading db true)
     :dispatch [::warehouse/load-current-user
                {:on-success [::load-account-success]}]}))

(re-frame/reg-event-fx
  ::load-account-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data]]
    {:db (-> db
             (set-account-loading false)
             (set-account-data data))}))

(re-frame/reg-event-fx
  ::open-accounts-list
  (fn [{:keys []} [_]]
    {:dispatch [::routes/redirect :accounts :account-type "live"]}))
