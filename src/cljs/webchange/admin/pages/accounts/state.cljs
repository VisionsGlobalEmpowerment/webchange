(ns webchange.admin.pages.accounts.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.validation.specs.account :as account-spec]
    [webchange.validation.validate :refer [validate]]))

(def path-to-db :page/accounts)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; Account type

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
  get-account-type)

(re-frame/reg-sub
  ::valid-account-type?
  :<- [::account-type]
  (fn [account-type]
    (->> account-type
         (validate ::account-spec/type)
         (nil?))))

;;

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [account-type]}]]
    {:db (-> db (set-account-type account-type))}))

(re-frame/reg-event-fx
  ::set-account-type
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ account-type]]
    {:db (-> db (set-account-type account-type))}))


(re-frame/reg-sub
  ::add-button
  :<- [::account-type]
  (fn [account-type]
    (case account-type
      "admin" {:title "Add Admin Account"}
      nil)))

(re-frame/reg-sub
  ::header
  :<- [::account-type]
  (fn [account-type]
    (case account-type
      "admin" "Admins"
      "live" "Users"
      "Accounts")))

(re-frame/reg-event-fx
  ::add-account
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [account-type (get-account-type db)]
      {:dispatch [::routes/redirect :account-add :account-type account-type]})))
