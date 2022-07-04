(ns webchange.admin.pages.account-add.state
  (:require
    [clojure.string :as str]
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]))

(def path-to-db :page/add-account)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; Account Type

(def account-type-key :account-type)

(defn- get-account-type
  [db]
  (get db account-type-key))

(defn- set-account-type
  [db value]
  (assoc db account-type-key value))

(re-frame/reg-sub
  ::account-type
  :<- [path-to-db]
  #(get-account-type %))

;;

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [account-type]}]]
    {:db (-> db (set-account-type account-type))}))

(re-frame/reg-event-fx
  ::open-accounts-list
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [account-type (get-account-type db)]
      {:dispatch [::routes/redirect :accounts :account-type account-type]})))
