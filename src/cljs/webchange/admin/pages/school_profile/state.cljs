(ns webchange.admin.pages.school-profile.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :school-profile)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [school-id]}]]
    {:db (-> db
             (assoc :school-id school-id)
             (dissoc :school-data))
     :dispatch [::warehouse/load-school {:school-id school-id}
                {:on-success [::load-school-success]}]}))

(re-frame/reg-event-fx
  ::load-school-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [school]}]]
    {:db (assoc db :school-data school)}))

(re-frame/reg-sub
  ::school-data
  :<- [path-to-db]
  #(get % :school-data))

(re-frame/reg-event-fx
  ::set-school-data
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data]]
    {:db (assoc db :school-data data)}))

(re-frame/reg-sub
  ::school-name
  :<- [::school-data]
  #(-> (get % :name "")
       (clojure.string/capitalize)))

(re-frame/reg-event-fx
  ::open-teachers
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [school-id (:school-id db)]
      {:dispatch [::routes/redirect :teachers :school-id school-id]})))

(re-frame/reg-event-fx
  ::open-students
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [school-id (:school-id db)]
      {:dispatch [::routes/redirect :students :school-id school-id]})))

(re-frame/reg-event-fx
  ::open-classes
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [school-id (:school-id db)]
      {:dispatch [::routes/redirect :classes :school-id school-id]})))

(re-frame/reg-event-fx
  ::open-courses
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [school-id (:school-id db)]
      {:dispatch [::routes/redirect :school-courses :school-id school-id]})))
