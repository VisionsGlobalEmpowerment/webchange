(ns webchange.admin.pages.students.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]
    [webchange.utils.date :refer [date-str->locale-date]]
    [webchange.utils.list :refer [update-by-predicate]]))

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

(defn- update-student
  [db id data-patch]
  (update db students-key update-by-predicate #(= (:id %) id) merge data-patch))

(re-frame/reg-sub
  ::students
  :<- [path-to-db]
  #(->> (get % students-key [])
        (sort-by :name)))

(re-frame/reg-sub
  ::students-number
  :<- [::students]
  #(count %))

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

(defn- ->students-list-item
  [{:keys [id status user]}]
  {:id         id
   :name       (str (:first-name user) " " (:last-name user))
   :email      (:email user)
   :active?    (when (some? status) (= status "active"))
   :last-login (date-str->locale-date "2022-05-18T11:33:36.316428")})

(re-frame/reg-event-fx
  ::load-school-students-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ students]]
    {:db (set-students db (map ->students-list-item students))}))

(re-frame/reg-sub
  ::school-data
  :<- [path-to-db]
  get-school-data)

(re-frame/reg-sub
  ::school-name
  :<- [::school-data]
  #(get % :name ""))

(re-frame/reg-event-fx
  ::add-student
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [school-id (:id (get-school-data db))]
      {:dispatch [::routes/redirect :student-add :school-id school-id]})))

(re-frame/reg-event-fx
  ::open-student-profile
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ student-id]]
    (let [school-id (:id (get-school-data db))]
      {:dispatch [::routes/redirect :student-profile :school-id school-id :student-id student-id]})))

(re-frame/reg-event-fx
  ::edit-student
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ student-id]]
    (let [school-id (:id (get-school-data db))]
      {:dispatch [::routes/redirect :student-profile :school-id school-id :student-id student-id
                  :storage-params {:action           "edit"
                                   :on-edit-finished [::edit-student-finished school-id]}]})))

(re-frame/reg-event-fx
  ::edit-student-finished
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_ school-id]]
    {:dispatch [::routes/redirect :students :school-id school-id]}))

(re-frame/reg-event-fx
  ::open-school-profile
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [school-id (:id (get-school-data db))]
      {:dispatch [::routes/redirect :school-profile :school-id school-id]})))

;; Active status

(re-frame/reg-event-fx
  ::set-student-status
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ student-id active?]]
    {:db       (update-student db student-id {:active? :loading})
     :dispatch [::warehouse/set-student-status
                {:student-id student-id
                 :active     active?}
                {:on-success [::set-student-status-success student-id active?]
                 :on-failure [::set-student-status-failure student-id (not active?)]}]}))

(re-frame/reg-event-fx
  ::set-student-status-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ student-id active?]]
    {:db (update-student db student-id {:active? active?})}))

(re-frame/reg-event-fx
  ::set-student-status-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ student-id active?]]
    {:db (update-student db student-id {:active? active?})}))
