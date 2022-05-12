(ns webchange.admin.pages.add-student.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :add-student)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-sub
  ::errors
  :<- [path-to-db]
  (fn [data]
    (get data :errors)))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [school-id]}]]
    {:db (-> db
             (assoc :saving false)
             (assoc :loading false)
             (assoc :school-id school-id))}))

(re-frame/reg-event-fx
  ::create-student
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data]]
    (let [school-id (:school-id db)]
      {:db (assoc db :saving true)
       :dispatch [::warehouse/create-student {:school-id school-id :data data}
                  {:on-success [::create-student-success]}]})))

(re-frame/reg-event-fx
  ::create-student-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [school-id (:school-id db)]
      {:db (assoc db :saving false)
       :dispatch [::routes/redirect :students :school-id school-id]})))

(re-frame/reg-event-fx
  ::generate-access-code
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [on-success]}]]
    (let [school-id (:school-id db)]
      {:db (assoc db :saving true)
       :dispatch [::warehouse/generate-access-code
                  {:on-success [::generate-access-code-success on-success]}]})))

(re-frame/reg-event-fx
  ::generate-access-code-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ on-success {:keys [access-code]}]]
    (on-success access-code)
    {:db (assoc db :saving false)}))

(re-frame/reg-sub
  ::class-options
  :<- [path-to-db]
  (fn [data]
    [{:value 1 :text "Stab class 1"}
     {:value 2 :text "Stab class 2"}]))

(re-frame/reg-sub
  ::data-loading?
  :<- [path-to-db]
  (fn [data]
    false))

(re-frame/reg-sub
  ::data-saving?
  :<- [path-to-db]
  (fn [data]
    false))
