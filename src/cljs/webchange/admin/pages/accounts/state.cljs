(ns webchange.admin.pages.accounts.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :accounts)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [db]} _]
    {:dispatch [::warehouse/load-accounts-by-type {:type "admin"}
                {:on-success [::load-accounts-success]}]}))

(defn- prepare-account
  [{:keys [first-name last-name] :as account}]
  (assoc account :name (str first-name " " last-name)))

(re-frame/reg-event-fx
  ::load-accounts-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [accounts current-page pages]}]]
    {:db (assoc db
           :accounts (map prepare-account accounts)
           :current-page current-page
           :pages pages)}))

(re-frame/reg-event-fx
  ::add-admin
  (fn [{:keys [db]} [_]]
    {:dispatch [::routes/redirect :add-account :type "admin"]}))

(re-frame/reg-event-fx
  ::edit-account
  (fn [{:keys [db]} [_ account-id]]
    {:dispatch [::routes/redirect :edit-account :account-id account-id]}))

(re-frame/reg-event-fx
  ::toggle-active
  (fn [{:keys [db]} [_ account-id active]]
    {:dispatch [::warehouse/set-account-status {:account-id account-id :active active}
                {:on-success [::toggle-success]}]}))

(re-frame/reg-event-fx
  ::toggle-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [id active]}]]
    (let [update-status (fn [{user-id :id :as account}]
                          (if (= id user-id)
                            (assoc account :active active)
                            account))]
      {:db (-> db
               (update :accounts #(map update-status %)))})))


(re-frame/reg-sub
  ::accounts-list
  :<- [path-to-db]
  (fn [data]
    (get data :accounts [])))

(re-frame/reg-sub
  ::current-page
  :<- [path-to-db]
  (fn [data]
    (get data :current-page 1)))

(re-frame/reg-sub
  ::pages
  :<- [path-to-db]
  (fn [data]
    (get data :pages)))
