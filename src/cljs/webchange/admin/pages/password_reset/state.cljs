(ns webchange.admin.pages.password-reset.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.utils.date :refer [date-str->locale-date]]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :page/password-reset)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-sub
  ::account-data
  :<- [path-to-db]
  #(get % :account-data))

(re-frame/reg-sub
  ::account-info
  :<- [::account-data]
  (fn [{:keys [first-name last-name created-at last-login]}]
    {:name (str first-name " " last-name)
     :account-created (date-str->locale-date created-at)
     :last-login (date-str->locale-date last-login)}))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [account-id]}]]
    {:db       (-> db
                   (assoc :loading true)
                   (assoc :account-id account-id))
     :dispatch [::warehouse/load-account {:id account-id}
                {:on-success [::load-account-success]}]}))

(re-frame/reg-event-fx
  ::load-account-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data]]
    {:db (-> db
             (assoc :loading false)
             (assoc :account-data data))}))

(re-frame/reg-event-fx
  ::open-accounts-list
  (fn [{:keys []} [_ result]]
    {:dispatch [::routes/redirect :accounts :account-type "live"]}))
