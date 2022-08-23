(ns webchange.admin.pages.class-profile.students-list.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.pages.class-profile.state :as parent-state]
    [webchange.admin.routes :as routes]
    [webchange.admin.widgets.confirm.state :as confirm]
    [webchange.state.warehouse :as warehouse]
    [webchange.utils.list :as lists]))

(def path-to-db :page/class-profile--students-list)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; school

(def school-id-key :school-id)

(defn- set-school-id
  [db value]
  (assoc db school-id-key value))

(defn- get-school-id
  [db]
  (get db school-id-key))

;; class

(def class-id-key :class-id)

(defn- set-class-id
  [db value]
  (assoc db class-id-key value))

(defn- get-class-id
  [db]
  (get db class-id-key))

;; students

(def students-key :students)

(defn- set-students
  [db data]
  (assoc db students-key data))

(defn- remove-student
  [db student-id]
  (update db students-key lists/remove-by-predicate #(= (:id %) student-id)))

(defn- ->students-list-item
  [{:keys [id user]}]
  {:id   id
   :name (str (:first-name user) " " (:last-name user))})

(re-frame/reg-sub
  ::students
  :<- [path-to-db]
  #(->> (get % students-key [])
        (map ->students-list-item)))

;; loading?

(def loading-key :loading?)

(defn- set-loading
  [db value]
  (assoc db loading-key value))

(re-frame/reg-sub
  ::loading?
  :<- [path-to-db]
  #(get % loading-key true))

;;

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [class-id school-id params]}]]
    {:db       (-> db
                   (set-school-id school-id)
                   (set-class-id class-id)
                   (set-loading true)
                   (assoc :on-finish (:on-edit-students-finished params)))
     :dispatch [::warehouse/load-class-students
                {:class-id class-id}
                {:on-success [::load-students-success]}]}))

(re-frame/reg-event-fx
  ::reset
  (fn [{:keys [db]} [_]]
    {:db (dissoc db path-to-db)}))

(re-frame/reg-event-fx
  ::load-students-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [students]}]]
    {:db (-> db
             (set-loading false)
             (set-students students))}))

(re-frame/reg-event-fx
  ::open-student-profile
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ student-id]]
    (let [school-id (get-school-id db)
          class-id (get-class-id db)]
      {:dispatch [::routes/redirect :student-profile :school-id school-id :class-id class-id :student-id student-id]})))

(re-frame/reg-event-fx
  ::edit-student
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ student-id]]
    (let [school-id (get-school-id db)
          class-id (get-class-id db)]
      {:dispatch [::routes/redirect :student-profile :school-id school-id :class-id class-id :student-id student-id
                  :storage-params {:action "edit"}]})))

;; remove student

(def student-removing-key :student-removing)

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
  (fn [{:keys [_]} [_ student-id]]
    {:dispatch [::confirm/show-confirm-window {:message    "Remove student from class?"
                                               :on-confirm [::remove-student-confirmed student-id]}]}))

(re-frame/reg-event-fx
  ::remove-student-confirmed
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ student-id]]
    (let [class-id (get-class-id db)]
      {:db       (-> db (set-student-removing student-id true))
       :dispatch [::warehouse/remove-student-from-class
                  {:class-id   class-id
                   :student-id student-id}
                  {:on-success [::remove-student-success student-id]
                   :on-failure [::remove-student-failure student-id]}]})))

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

(re-frame/reg-event-fx
  ::close
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [on-finish (:on-finish db)]
      {:dispatch-n (cond-> [[::parent-state/load-class]
                            [::parent-state/open-class-form]]
                           (some? on-finish) (conj on-finish))})))
