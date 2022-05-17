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

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [school-id]}]]
    {:db       (-> db
                   (assoc :saving false)
                   (assoc :loading true)
                   (assoc :school-id school-id))
     :dispatch [::warehouse/load-school-classes {:school-id school-id}
                {:on-success [::load-school-classes-success]}]}))

(re-frame/reg-event-fx
  ::load-school-classes-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [classes]}]]
    (let [school-id (:school-id db)
          class-options (->> classes
                             (map (fn [{:keys [id name]}] {:value id :text name})))]
      {:db (-> db
               (assoc :loading false)
               (assoc :class-options class-options))})))

(re-frame/reg-event-fx
  ::create-student
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data]]
    (let [school-id (:school-id db)]
      {:db       (assoc db :saving true)
       :dispatch [::warehouse/create-student {:school-id school-id :data data}
                  {:on-success [::create-student-success]
                   :on-failure [::create-student-failure]}]})))

(re-frame/reg-event-fx
  ::create-student-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [school-id (:school-id db)]
      {:db       (assoc db :saving false)
       :dispatch [::routes/redirect :students :school-id school-id]})))

(re-frame/reg-event-fx
  ::create-student-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (assoc db :saving false)}))

(re-frame/reg-event-fx
  ::generate-access-code
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [on-success]}]]
    (let [school-id (:school-id db)]
      {:db       (assoc db :saving true)
       :dispatch [::warehouse/generate-school-access-code {:school-id school-id}
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
    (get data :class-options)))

(re-frame/reg-sub
  ::data-loading?
  :<- [path-to-db]
  (fn [data]
    (get data :loading false)))

(re-frame/reg-sub
  ::data-saving?
  :<- [path-to-db]
  (fn [data]
    (get data :saving)))
