(ns webchange.admin.widgets.account-form.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.ui.components.form.data :refer [init]]
    [webchange.admin.routes :as routes]
    [webchange.admin.widgets.state :as widgets]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :widget/account-form)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; Form Data

(def form-data (init :form-data))
(def get-form-data (:get-data form-data))
(def set-form-data (:set-data form-data))
(def reset-form-data (:reset-data form-data))
(def update-form-data (:update-data form-data))

(re-frame/reg-sub
  ::form-data
  :<- [path-to-db]
  #(get-form-data %))

;; Data Saving?

(def data-saving-key :data-saving?)

(defn- set-data-saving
  [db value]
  (assoc db data-saving-key value))

(re-frame/reg-sub
  ::data-saving?
  :<- [path-to-db]
  #(get % data-saving-key false))

;; Custom Error

(def custom-errors-key :custom-errors)

(defn- set-custom-errors
  [db errors]
  (assoc db custom-errors-key errors))

(re-frame/reg-sub
  ::custom-errors
  :<- [path-to-db]
  #(get % custom-errors-key {}))

;;

(re-frame/reg-event-fx
  ::init-add-form
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [_]}]]
    {:db (reset-form-data db)}))

(re-frame/reg-event-fx
  ::init-edit-form
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [account-id] :as props}]]
    {:db       (-> db
                   (reset-form-data)
                   (widgets/set-callbacks (select-keys props [:on-remove]))
                   (assoc :account-id account-id))
     :dispatch [::warehouse/load-account {:id account-id}
                {:on-success [::load-account-success]}]}))

(re-frame/reg-event-fx
  ::load-account-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data]]
    {:db (-> db (set-form-data data))}))

(re-frame/reg-event-fx
  ::reset-form
  (fn [{:keys [db]} [_ {:keys [_]}]]
    {:db (dissoc db path-to-db)}))

(re-frame/reg-event-fx
  ::create-account
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [password password-confirm] :as data} {:keys [on-success]}]]
    (if (not= password password-confirm)
      {:db (set-custom-errors db {:password-confirm "Passwords not equal"})}
      {:db       (-> db
                     (set-data-saving true)
                     (set-custom-errors nil))
       :dispatch [::warehouse/create-account data
                  {:on-success [::create-account-success on-success]
                   :on-failure [::create-account-failure]}]})))

(re-frame/reg-event-fx
  ::create-account-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ success-handler response]]
    {:db                (set-data-saving db false)
     ::widgets/callback [success-handler response]}))

(re-frame/reg-event-fx
  ::create-account-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-data-saving db false)}))

(re-frame/reg-event-fx
  ::edit-account
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ account-id data {:keys [on-success]}]]
    {:db       (set-data-saving db true)
     :dispatch [::warehouse/save-account
                {:id   account-id
                 :data data}
                {:on-success [::edit-account-success on-success]
                 :on-failure [::edit-account-failure]}]}))

(re-frame/reg-event-fx
  ::edit-account-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ success-handler response]]
    {:db                (set-data-saving db false)
     ::widgets/callback [success-handler response]}))

(re-frame/reg-event-fx
  ::edit-account-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-data-saving db false)}))

;; Remove

(def remove-window-state-key :remove-window-state)

(def remove-window-default-state {:open?        false
                                  :in-progress? false
                                  :done?        false})

(defn- update-remove-window-state
  [db data-patch]
  (update db remove-window-state-key merge data-patch))

(re-frame/reg-sub
  ::remove-window-state
  :<- [path-to-db]
  #(get % remove-window-state-key remove-window-default-state))

(re-frame/reg-event-fx
  ::open-remove-window
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (update-remove-window-state db {:open? true})}))

(re-frame/reg-event-fx
  ::close-remove-window
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (update-remove-window-state db remove-window-default-state)}))

(re-frame/reg-event-fx
  ::handle-removed
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [success-handler (widgets/get-callback db :on-remove)]
      {:dispatch  [::close-remove-window]
       ::widgets/callback [success-handler]})))

;; Remove Account

(def account-removing-key :account-removing?)

(defn- set-account-removing
  [db value]
  (assoc db account-removing-key value))

(re-frame/reg-sub
  ::account-removing?
  :<- [path-to-db]
  #(get % account-removing-key false))

(re-frame/reg-event-fx
  ::remove-account
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ account-id]]
    {:db       (set-account-removing db true)
     :dispatch [::warehouse/delete-account
                {:id account-id}
                {:on-success [::remove-account-success]
                 :on-failure [::remove-account-failure]}]}))

(re-frame/reg-event-fx
  ::remove-account-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ response]]
    {:db                (-> db
                            (set-account-removing false)
                            (update-remove-window-state {:done? true}))}))

(re-frame/reg-event-fx
  ::remove-account-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-account-removing db false)}))


;; Reset Password

(re-frame/reg-event-fx
  ::reset-password
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [account-id (:account-id db)]
      {:dispatch [::routes/redirect :password-reset :account-id account-id]})))
