(ns webchange.admin.widgets.account-form.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.components.form.data :refer [init]]
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

;;

(re-frame/reg-event-fx
  ::init-add-form
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [_]}]]
    {:db (reset-form-data db)}))

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
  (fn [{:keys [db]} [_ data {:keys [on-success]}]]
    (print "::edit-account" data)
    {}
    #_{:db       (set-data-saving db true)
     :dispatch [::warehouse/create-account data
                {:on-success [::create-account-success on-success]
                 :on-failure [::create-account-failure]}]}))