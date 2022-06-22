(ns webchange.admin.widgets.account-form.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.ui.components.form.data :refer [init]]
    [webchange.admin.routes :as routes]
    [webchange.admin.widgets.state :as widgets]
    [webchange.state.warehouse :as warehouse]
    [webchange.utils.date :refer [date-str->locale-date]]))

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

;; Account Info

(re-frame/reg-sub
  ::account-info
  :<- [::form-data]
  (fn [{:keys [created-at last-login] :as account-data}]
    (if (some? account-data)
      [["Account Created" (date-str->locale-date created-at)]
       ["Last Login" (date-str->locale-date last-login)]]
      [])))

;;

(re-frame/reg-event-fx
  ::init-add-form
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [_]}]]
    {:db (reset-form-data db)}))

(re-frame/reg-event-fx
  ::init-edit-form
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [account-id]}]]
    {:db       (reset-form-data db)
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
  (fn [{:keys [db]} [_ data {:keys [on-success]}]]
    {:db       (set-data-saving db true)
     :dispatch [::warehouse/create-account data
                {:on-success [::create-account-success on-success]
                 :on-failure [::create-account-failure]}]}))

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
  (fn [{:keys [db]} [_ account-id {:keys [on-success]}]]
    {:db       (set-account-removing db true)
     :dispatch [::warehouse/delete-account
                {:id account-id}
                {:on-success [::remove-account-success on-success]
                 :on-failure [::remove-account-failure]}]}))

(re-frame/reg-event-fx
  ::remove-account-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ on-success response]]
    {:db                (set-account-removing db false)
     ::widgets/callback [on-success response]}))

(re-frame/reg-event-fx
  ::remove-account-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-account-removing db false)}))

;; Reset Password

(re-frame/reg-event-fx
  ::reset-password
  (fn [{:keys [_]} [_ account-id]]
    {:dispatch [::routes/redirect :password-reset :account-id account-id]}))
