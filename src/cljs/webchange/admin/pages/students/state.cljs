(ns webchange.admin.pages.students.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]
    [webchange.utils.list :refer [remove-by-predicate]]))

(def path-to-db :school-students)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(defn- get-school-data
  [db]
  (get db :school-data))

;; students

(def students-key :students)

(defn- set-students
  [db value]
  (assoc db students-key (vec value)))

(re-frame/reg-sub
  ::students
  :<- [path-to-db]
  #(->> (get % students-key [])
        (sort-by :name)))

(defn- remove-student
  [db student-id]
  (update db students-key remove-by-predicate #(= (:id %) student-id)))

;;

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
      {:db (set-students db students-list)})))

(re-frame/reg-sub
  ::school-data
  :<- [path-to-db]
  get-school-data)

(re-frame/reg-sub
  ::school-name
  :<- [::school-data]
  :name)

(re-frame/reg-event-fx
  ::add-student
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [school-id (:id (get-school-data db))]
      {:dispatch [::routes/redirect :student-add :school-id school-id]})))

(re-frame/reg-event-fx
  ::edit-student
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ student-id]]
    (let [school-id (:id (get-school-data db))]
      {:dispatch [::routes/redirect :student-edit :school-id school-id :student-id student-id]})))

;; remove student

(def student-removing-key :student-removing?)

(defn- set-student-removing
  [db student-id value]
  (assoc-in db [student-removing-key student-id] value))

(re-frame/reg-sub
  ::student-removing?
  :<- [path-to-db]
  (fn [db [_ student-id]]
    (get-in db [student-removing-key student-id] false)))

(re-frame/reg-event-fx
  ::remove-student
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ student-id]]
    {:db       (set-student-removing db student-id true)
     :dispatch [::warehouse/delete-student
                {:student-id student-id}
                {:on-success [::remove-student-success student-id]
                 :on-failure [::remove-student-failure student-id]}]}))

(re-frame/reg-event-fx
  ::remove-student-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ student-id]]
    {:db (-> db
             (set-student-removing student-id false)
             (remove-student student-id))}))

(re-frame/reg-event-fx
  ::remove-student-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ student-id]]
    {:db (-> db (set-student-removing student-id false))}))
