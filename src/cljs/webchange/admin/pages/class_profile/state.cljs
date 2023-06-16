(ns webchange.admin.pages.class-profile.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]
    [webchange.utils.date :as date]))

(def path-to-db :page/class-profile)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; Side Bar Content

(def side-bar-key :side-bar)

(defn- set-side-bar
  ([db component]
   (assoc db side-bar-key {:component component}))
  ([db component props]
   (assoc db side-bar-key {:component component
                           :props     props})))

(re-frame/reg-sub
  ::side-bar
  :<- [path-to-db]
  #(get % side-bar-key {:component :class-form}))

(re-frame/reg-event-fx
  ::open-add-student-form
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-side-bar db :students-add)}))

(re-frame/reg-event-fx
  ::open-students-list
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-side-bar db :students-list)}))

(re-frame/reg-event-fx
  ::open-add-teacher-form
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-side-bar db :teachers-add)}))

(re-frame/reg-event-fx
  ::open-edit-teacher-form
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ teacher-id]]
    {:db (set-side-bar db :teacher-edit {:teacher-id teacher-id})}))

(re-frame/reg-event-fx
  ::open-teachers-list
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-side-bar db :teachers-list)}))

(re-frame/reg-event-fx
  ::open-class-form
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-side-bar db :class-form)}))

(re-frame/reg-event-fx
  ::open-assign-course-form
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-side-bar db :assign-course)}))

;; Class Form

(def form-editable-key :form-editable?)

(re-frame/reg-sub
  ::form-editable?
  :<- [path-to-db]
  #(get % form-editable-key false))

(defn- set-form-editable
  [db value]
  (assoc db form-editable-key value))

(re-frame/reg-event-fx
  ::set-form-editable
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (set-form-editable db value)}))

(re-frame/reg-event-fx
  ::handle-class-edit-cancel
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [on-edit-finished]} (:handlers db)]
      {:dispatch-n (cond-> [[::set-form-editable false]]
                           (some? on-edit-finished) (conj on-edit-finished))})))

;; Class data

(re-frame/reg-sub
  ::class-data
  :<- [path-to-db]
  :class-data)

(defn- set-class-data
  [db class-data]
  (assoc db :class-data class-data))

(defn- update-class-data
  [db class-data]
  (update db :class-data merge class-data))

(re-frame/reg-event-fx
  ::update-class-data
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data]]
    (let [{:keys [on-edit-finished]} (:handlers db)]
      (cond-> {:db (-> db
                       (update-class-data data)
                       (set-form-editable false))}
              (some? on-edit-finished) (assoc :dispatch on-edit-finished)))))

;; school courses

(def school-courses-key :school-courses)

(defn- set-school-courses
  [db data]
  (assoc db school-courses-key data))

(re-frame/reg-sub
  ::school-courses
  :<- [path-to-db]
  #(get % school-courses-key []))

(re-frame/reg-sub
  ::school-courses-number
  :<- [::school-courses]
  #(count %))

;;

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [class-id school-id params]}]]
    (let [{:keys [action]} params]
      {:db         (-> db
                       (assoc :school-id school-id)
                       (assoc :class-id class-id)
                       (assoc :handlers (select-keys params [:on-edit-finished])))
       :dispatch-n (cond-> [[::load-class]
                            [::warehouse/load-school-courses {:school-id school-id}
                             {:on-success [::load-school-courses-success]}]
                            [::warehouse/load-school {:school-id school-id}
                             {:on-success [::load-school-success]}]
                            [::warehouse/load-class-stats {:class-id class-id}
                             {:on-success [::load-class-stats-success]}]]
                           (= action "edit") (conj [::set-form-editable true])
                           (= action "manage-students") (conj [::open-students-list])
                           (= action "manage-teachers") (conj [::open-teachers-list]))})))

(re-frame/reg-event-fx
  ::load-class
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [class-id (:class-id db)]
      {:dispatch [::warehouse/load-class {:class-id class-id}
                  {:on-success [::load-class-success]}]})))

(re-frame/reg-event-fx
  ::load-class-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [class]}]]
    {:db (set-class-data db class)}))

(re-frame/reg-event-fx
  ::load-school-courses-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ courses]]
    {:db (set-school-courses db courses)}))

(re-frame/reg-event-fx
  ::load-class-stats-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ stats]]
    (let [stats-data (update stats :time-spent date/ms->duration)]
      {:db (assoc db :class-activities-stats stats-data)})))

(re-frame/reg-event-fx
  ::load-school-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [school]}]]
    {:db (assoc db :school-data school)}))

(re-frame/reg-sub
  ::readonly?
  :<- [path-to-db]
  #(get-in % [:school-data :readonly] false))

(re-frame/reg-sub
  ::personal?
  :<- [path-to-db]
  #(-> (get-in % [:school-data :type])
       (= "personal")))

(re-frame/reg-sub
  ::class-stats
  :<- [::class-data]
  #(get % :stats {}))

(re-frame/reg-sub
  ::class-course
  :<- [::class-data]
  #(get % :course-info))

(re-frame/reg-sub
  ::class-activities-stats
  :<- [path-to-db]
  #(get % :class-activities-stats))

(re-frame/reg-event-fx
  ::handle-students-added
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [class]}]]
    {:db (-> db
             (update-class-data (select-keys class [:stats]))
             (set-side-bar :class-form))}))

(re-frame/reg-event-fx
  ::handle-teachers-added
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [class]}]]
    {:db (-> db
             (update-class-data (select-keys class [:stats]))
             (set-side-bar :class-form))}))

(re-frame/reg-event-fx
  ::open-students-activities
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [class-id school-id]} db]
      {:dispatch [::routes/redirect :class-students :school-id school-id :class-id class-id]})))

(re-frame/reg-event-fx
  ::handle-class-deleted
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [school-id]} db]
      {:dispatch [::routes/redirect :school-profile :school-id school-id]})))
