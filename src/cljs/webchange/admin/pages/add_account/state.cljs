(ns webchange.admin.pages.add-account.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :add-account)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [type]}]]
    {:db (-> db
             (assoc :type type))}))

(re-frame/reg-event-fx
  ::create-account
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data]]
    {:db (assoc db :saving true)
     :dispatch [::warehouse/create-account data
                {:on-success [::create-account-success]}]}))

(re-frame/reg-event-fx
  ::create-account-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (assoc db :saving false)
     :dispatch [::routes/redirect :accounts]}))

(re-frame/reg-sub
  ::data-saving?
  :<- [path-to-db]
  (fn [data]
    (get data :saving)))
