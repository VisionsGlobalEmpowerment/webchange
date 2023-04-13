(ns webchange.admin.pages.teacher-transfer.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :page/teacher-transfer)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [school-id]}]]
    {:db (-> db (assoc :school-id school-id))}))

(re-frame/reg-event-fx
  ::open-teachers-list
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [school-id (:school-id db)]
      {:dispatch [::routes/redirect :teachers :school-id school-id]})))

(re-frame/reg-event-fx
  ::set-email
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (-> db
             (assoc :email value)
             (dissoc :error))}))

(re-frame/reg-sub
  ::email-value
  :<- [path-to-db]
  #(get % :email))

(re-frame/reg-sub
  ::error-value
  :<- [path-to-db]
  #(get % :error))

(re-frame/reg-event-fx
  ::apply
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [school-id (get db :school-id)
          email (get db :email)]
      {:db (assoc db :loading true)
       :dispatch [::warehouse/transfer-teacher
                  {:school-id school-id
                   :data {:email email}}
                  {:on-success [::transfer-teacher-success]
                   :on-failure [::transfer-teacher-failure]}]})))

(re-frame/reg-event-fx
  ::transfer-teacher-success
  [(i/path path-to-db)]
  (fn [{:keys [_db]} [_ ]]
    {:dispatch [::open-teachers-list]}))

(re-frame/reg-event-fx
  ::transfer-teacher-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ ]]
    {:db (assoc db :error "Teacher transfer failed. Please check provided email.")}))
