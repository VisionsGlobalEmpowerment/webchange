(ns webchange.admin.widgets.accounts-list.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]
    [webchange.utils.date :refer [date-str->locale-date]]))

(def path-to-db :widget/accounts-list)

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
  #(get % account-type-key))

;; Accounts

(def accounts-key :accounts)

(defn- set-accounts
  [db data]
  (assoc db accounts-key data))

(defn- update-accounts
  [db handler]
  (update db accounts-key #(map handler %)))

(defn- prepare-account
  [{:keys [first-name last-name] :as account}]
  (-> account
      (assoc :name (str first-name " " last-name))
      (update :created-at date-str->locale-date)
      (update :last-login date-str->locale-date)
      (assoc :active? (:active account))
      (dissoc :active)))

(re-frame/reg-sub
  ::accounts
  :<- [path-to-db]
  (fn [db]
    (->> (get db accounts-key [])
         (map prepare-account))))

;; Pagination

(def pagination-key :pagination)

(defn- set-pagination
  [db data]
  (assoc db pagination-key data))

(re-frame/reg-sub
  ::pagination
  :<- [path-to-db]
  (fn [db]
    (get db pagination-key {:current 1
                            :total   1})))

;;

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [account-type]}]]
    {:db       (-> db (set-account-type account-type))
     :dispatch [::load-accounts-page 1]}))

(re-frame/reg-event-fx
  ::set-account-type
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ account-type]]
    (let [current-account-type (get-account-type db)]
      (when (not= account-type current-account-type)
        {:db       (-> db (set-account-type account-type))
         :dispatch [::load-accounts-page 1]}))))

(re-frame/reg-event-fx
  ::load-accounts-page
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ page]]
    (let [account-type (get-account-type db)]
      {:db       (-> db (set-loading true))
       :dispatch [::warehouse/load-accounts-by-type {:type account-type
                                                     :page page}
                  {:on-success [::load-accounts-success]
                   :on-failure [::load-accounts-failure]}]})))

(re-frame/reg-event-fx
  ::load-accounts-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [accounts current-page pages]}]]
    {:db (-> db
             (set-loading false)
             (set-accounts accounts)
             (set-pagination {:current current-page
                              :total   pages}))}))

(re-frame/reg-event-fx
  ::load-accounts-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db (set-loading false))}))

(re-frame/reg-event-fx
  ::toggle-active
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ account-id active?]]
    {:db       (-> db
                   (update-accounts (fn [{user-id :id :as account}]
                                      (if (= account-id user-id)
                                        (assoc account :loading? true)
                                        account))))
     :dispatch [::warehouse/set-account-status
                {:account-id account-id
                 :active     active?}
                {:on-success [::toggle-active-success]}]}))

(re-frame/reg-event-fx
  ::toggle-active-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [id active]}]]
    (let [update-status (fn [{user-id :id :as account}]
                          (if (= id user-id)
                            (assoc account :active active :loading? false)
                            account))]
      {:db (-> db (update-accounts update-status))})))

(re-frame/reg-event-fx
  ::edit-account
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_ account-id]]
    {:dispatch [::routes/redirect :account-edit :account-id account-id
                :storage-params {:action "edit"}]}))

(re-frame/reg-event-fx
  ::view-account
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_ account-id]]
    {:dispatch [::routes/redirect :account-edit :account-id account-id]}))
