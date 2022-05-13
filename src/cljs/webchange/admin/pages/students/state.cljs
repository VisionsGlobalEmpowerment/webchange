(ns webchange.admin.pages.students.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :school-students)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(defn- get-school-data
  [db]
  (get db :school-data))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_ {:keys [school-id]}]]
    {:dispatch-n [[::warehouse/load-school {:school-id school-id} {:on-success [::load-school-success]}]
                  [::warehouse/load-school-students {:school-id school-id} {:on-success [::load-school-students-success]}]]}))

(re-frame/reg-event-fx
  ::load-school-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [school]}]]
    {:db (assoc db :school-data school)}))

(re-frame/reg-event-fx
  ::load-school-students-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ students]]
    (let [->list-item #(assoc % :name (str (-> % :user :first-name) " " (-> % :user :last-name)))
          students-list (map ->list-item students)]
      {:db (assoc db :students students-list)})))

(re-frame/reg-sub
  ::school-data
  :<- [path-to-db]
  get-school-data)

(re-frame/reg-sub
  ::school-name
  :<- [::school-data]
  :name)

(re-frame/reg-sub
  ::students
  :<- [path-to-db]
  (fn [data]
    (get data :students [])))

(re-frame/reg-event-fx
  ::add-student
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [school-id (:id (get-school-data db))]
      {:dispatch [::routes/redirect :add-student :school-id school-id]})))

(re-frame/reg-event-fx
  ::edit-student
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ student-id]]
    (let [school-id (:id (get-school-data db))]
      {:dispatch [::routes/redirect :edit-student :school-id school-id :student-id student-id]})))

(re-frame/reg-event-fx
  ::remove-student
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_ student-id]]
    (print "::remove-student" student-id)))
