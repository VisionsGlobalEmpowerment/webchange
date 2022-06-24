(ns webchange.admin.widgets.teacher-form.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.ui.components.form.data :refer [init]]
    [webchange.admin.widgets.state :as widgets]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :widget/teacher-form)

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
  (fn [{:keys [db]} [_ {:keys [school-id]}]]
    {:db (-> db
             (assoc :school-id school-id)
             (set-custom-errors nil))}))

(re-frame/reg-event-fx
  ::init-edit-form
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [teacher-id]}]]
    {:db       (-> db (set-custom-errors nil))
     :dispatch [::warehouse/load-teacher {:teacher-id teacher-id}
                {:on-success [::load-teacher-success]}]}))

(re-frame/reg-event-fx
  ::load-teacher-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [teacher]}]]
    (let [teacher-data (-> (:user teacher)
                           (merge teacher))]
      {:db (-> db (set-form-data teacher-data))})))

(re-frame/reg-event-fx
  ::reset-form
  (fn [{:keys [db]} [_ {:keys [_]}]]
    {:db (dissoc db path-to-db)}))

;; create teacher

(re-frame/reg-event-fx
  ::create-teacher
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [password password-confirm] :as data} {:keys [on-success]}]]
    (if (not= password password-confirm)
      {:db (set-custom-errors db {:password-confirm "Passwords not equal"})}
      (let [school-id (:school-id db)]
        {:db       (-> db
                       (set-data-saving true)
                       (set-custom-errors nil))
         :dispatch [::warehouse/create-teacher {:school-id school-id :data data}
                    {:on-success [::create-teacher-success on-success]
                     :on-failure [::create-teacher-failure]}]}))))

(re-frame/reg-event-fx
  ::create-teacher-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ success-handler response]]
    {:db                (set-data-saving db false)
     ::widgets/callback [success-handler response]}))

(re-frame/reg-event-fx
  ::create-teacher-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-data-saving db false)}))

;; edit teacher

(re-frame/reg-event-fx
  ::edit-teacher
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ teacher-id data {:keys [on-success]}]]
    {:db       (set-data-saving db true)
     :dispatch [::warehouse/edit-teacher
                {:teacher-id teacher-id
                 :data       data}
                {:on-success [::edit-teacher-success on-success]
                 :on-failure [::edit-teacher-failure]}]}))

(re-frame/reg-event-fx
  ::edit-teacher-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ success-handler response]]
    {:db                (set-data-saving db false)
     ::widgets/callback [success-handler response]}))

(re-frame/reg-event-fx
  ::edit-teacher-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-data-saving db false)}))

;; Remove teacher

(def teacher-removing-key :teacher-removing?)

(defn- set-teacher-removing
  [db value]
  (assoc db teacher-removing-key value))

(re-frame/reg-sub
  ::teacher-removing?
  :<- [path-to-db]
  #(get % teacher-removing-key false))

(re-frame/reg-event-fx
  ::remove-teacher
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ teacher-id {:keys [on-success]}]]
    {:db       (set-teacher-removing db true)
     :dispatch [::warehouse/remove-teacher
                {:teacher-id teacher-id}
                {:on-success [::remove-teacher-success on-success]
                 :on-failure [::remove-teacher-failure]}]}))

(re-frame/reg-event-fx
  ::remove-teacher-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ on-success response]]
    {:db                (set-teacher-removing db false)
     ::widgets/callback [on-success response]}))

(re-frame/reg-event-fx
  ::remove-teacher-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-teacher-removing db false)}))
