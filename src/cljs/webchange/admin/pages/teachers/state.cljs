(ns webchange.admin.pages.teachers.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]
    [webchange.utils.date :refer [date-str->locale-date]]
    [webchange.utils.list :refer [remove-by-predicate update-by-predicate]]))

(def path-to-db :page/school-teachers)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(defn- get-school-data
  [db]
  (get db :school-data))


;; teachers

(def teachers-key :teachers)

(defn- set-teachers
  [db value]
  (assoc db teachers-key (vec value)))

(defn- update-teacher
  [db id data-patch]
  (update db teachers-key update-by-predicate #(= (:id %) id) merge data-patch))

(re-frame/reg-sub
  ::teachers
  :<- [path-to-db]
  #(->> (get % teachers-key [])
        (sort-by :name)))

(re-frame/reg-sub
  ::teachers-number
  :<- [::teachers]
  #(count %))

;;

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_ {:keys [school-id]}]]
    {:dispatch-n [[::warehouse/load-school {:school-id school-id} {:on-success [::load-school-success]}]
                  [::warehouse/load-school-teachers {:school-id school-id} {:on-success [::load-school-teachers-success]}]]}))

(re-frame/reg-event-fx
  ::load-school-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [school]}]]
    {:db (assoc db :school-data school)}))

(defn- ->teachers-list-item
  [{:keys [id status user]}]
  {:id         id
   :name       (str (:first-name user) " " (:last-name user))
   :email      (:email user)
   :active?    (when (some? status) (= status "active"))
   :last-login (date-str->locale-date "2022-05-18T11:33:36.316428")})

(re-frame/reg-event-fx
  ::load-school-teachers-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ teachers]]
    {:db (set-teachers db (map ->teachers-list-item teachers))}))

(re-frame/reg-sub
  ::school-data
  :<- [path-to-db]
  get-school-data)

(re-frame/reg-sub
  ::school-name
  :<- [::school-data]
  #(get % :name))

(re-frame/reg-event-fx
  ::add-teacher
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [school-id (:id (get-school-data db))]
      {:dispatch [::routes/redirect :teacher-add :school-id school-id]})))

(re-frame/reg-event-fx
  ::edit-teacher
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ teacher-id]]
    (let [school-id (:id (get-school-data db))]
      {:dispatch [::routes/redirect :teacher-profile :school-id school-id :teacher-id teacher-id]})))

(re-frame/reg-event-fx
  ::set-teacher-status
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ teacher-id active?]]
    {:db       (update-teacher db teacher-id {:active? :loading})
     :dispatch [::warehouse/set-teacher-status
                {:teacher-id teacher-id
                 :active     active?}
                {:on-success [::set-teacher-status-success teacher-id active?]
                 :on-failure [::set-teacher-status-failure teacher-id (not active?)]}]}))

(re-frame/reg-event-fx
  ::set-teacher-status-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ teacher-id active?]]
    {:db (update-teacher db teacher-id {:active? active?})}))

(re-frame/reg-event-fx
  ::set-teacher-status-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ teacher-id active?]]
    {:db (update-teacher db teacher-id {:active? active?})}))
