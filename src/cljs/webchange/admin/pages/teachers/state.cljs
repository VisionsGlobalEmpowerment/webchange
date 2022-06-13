(ns webchange.admin.pages.teachers.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]
    [webchange.utils.list :refer [remove-by-predicate]]))

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

(defn- remove-teacher
  [db teacher-id]
  (update db teachers-key remove-by-predicate #(= (:id %) teacher-id)))

(re-frame/reg-sub
  ::teachers
  :<- [path-to-db]
  #(->> (get % teachers-key [])
        (sort-by :name)))

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

(re-frame/reg-event-fx
  ::load-school-teachers-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ teachers]]
    (let [->list-item #(assoc % :name (str (-> % :user :first-name) " " (-> % :user :last-name)))
          teachers-list (map ->list-item teachers)]
      {:db (set-teachers db teachers-list)})))

(re-frame/reg-sub
  ::school-data
  :<- [path-to-db]
  get-school-data)

(re-frame/reg-sub
  ::school-name
  :<- [::school-data]
  :name)

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

;; remove teacher

(def teacher-removing-key :teacher-removing?)

(defn- set-teacher-removing
  [db teacher-id value]
  (assoc-in db [teacher-removing-key teacher-id] value))

(re-frame/reg-sub
  ::teacher-removing?
  :<- [path-to-db]
  (fn [db [_ teacher-id]]
    (get-in db [teacher-removing-key teacher-id] false)))

(re-frame/reg-event-fx
  ::remove-teacher
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ teacher-id]]
    {:db       (set-teacher-removing db teacher-id true)
     :dispatch [::warehouse/remove-teacher {:teacher-id teacher-id} {:on-success [::remove-teacher-success teacher-id]
                                                                     :on-failure [::remove-teacher-failure teacher-id]}]}))

(re-frame/reg-event-fx
  ::remove-teacher-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ teacher-id]]
    {:db (-> db
             (set-teacher-removing teacher-id false)
             (remove-teacher teacher-id))}))

(re-frame/reg-event-fx
  ::remove-teacher-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ teacher-id]]
    {:db (-> db (set-teacher-removing teacher-id false))}))
