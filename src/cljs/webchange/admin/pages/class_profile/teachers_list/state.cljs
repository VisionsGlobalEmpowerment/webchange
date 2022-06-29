(ns webchange.admin.pages.class-profile.teachers-list.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.pages.class-profile.state :as parent-state]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]
    [webchange.utils.list :as lists]))

(def path-to-db :page/class-profile--teachers-list)

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

;; teachers

(def teachers-key :teachers)

(defn- set-teachers
  [db data]
  (assoc db teachers-key data))

(defn- remove-teacher
  [db teacher-id]
  (update db teachers-key lists/remove-by-predicate #(= (:id %) teacher-id)))

(defn- ->teachers-list-item
  [{:keys [id user]}]
  {:id   id
   :name (str (:first-name user) " " (:last-name user))})

(re-frame/reg-sub
  ::teachers
  :<- [path-to-db]
  #(->> (get % teachers-key [])
        (map ->teachers-list-item)))

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
  (fn [{:keys [db]} [_ {:keys [class-id school-id]}]]
    {:db       (-> db
                   (set-school-id school-id)
                   (set-class-id class-id)
                   (set-loading true))
     :dispatch [::warehouse/load-class-teachers
                {:class-id class-id}
                {:on-success [::load-teachers-success]}]}))

(re-frame/reg-event-fx
  ::reset
  (fn [{:keys [db]} [_]]
    {:db (dissoc db path-to-db)}))

(re-frame/reg-event-fx
  ::load-teachers-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ teachers]]
    {:db (-> db
             (set-loading false)
             (set-teachers teachers))}))

(re-frame/reg-event-fx
  ::edit-teacher
  (fn [{:keys [db]} [_ teacher-id]]
    (let [school-id (get-school-id db)]
      {:dispatch [::routes/redirect :teacher-profile :school-id school-id :teacher-id teacher-id]})))

;; remove teacher

(def teacher-removing-key :teacher-removing)

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
    (let [class-id (get-class-id db)]
      {:db       (-> db (set-teacher-removing teacher-id true))
       :dispatch [::warehouse/remove-teacher-from-class
                  {:class-id   class-id
                   :teacher-id teacher-id}
                  {:on-success [::remove-teacher-success teacher-id]
                   :on-failure [::remove-teacher-failure teacher-id]}]})))

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

(re-frame/reg-event-fx
  ::close
  (fn [{:keys [_]} [_]]
    {:dispatch-n [[::parent-state/load-class]
                  [::parent-state/open-class-form]]}))
