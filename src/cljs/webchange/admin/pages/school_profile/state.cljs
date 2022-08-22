(ns webchange.admin.pages.school-profile.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]
    [webchange.admin.widgets.breadcrumbs.state :as breadcrumbs]))

(def path-to-db :school-profile)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [school-id url-params]}]]
    (let [edit? (-> url-params :edit (= "true"))]
      {:db         (-> db
                       (assoc :school-id school-id)
                       (dissoc :school-data))
       :dispatch-n (cond-> [[::warehouse/load-school {:school-id school-id}
                             {:on-success [::load-school-success]}]]
                           edit? (conj [::set-school-form-editable true]))})))

(re-frame/reg-event-fx
  ::reset
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_]]
    {:dispatch [::breadcrumbs/reset-current-node]}))

(re-frame/reg-event-fx
  ::load-school-success
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_ {:keys [school]}]]
    {:dispatch [::set-school-data school]}))

;; School Form

(def school-form-editable-key :school-form-editable?)

(re-frame/reg-sub
  ::school-form-editable?
  :<- [path-to-db]
  #(get % school-form-editable-key false))

(defn- set-school-form-editable
  [db value]
  (assoc db school-form-editable-key value))

(re-frame/reg-event-fx
  ::set-school-form-editable
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (set-school-form-editable db value)}))

;; School Data

(re-frame/reg-sub
  ::school-data
  :<- [path-to-db]
  #(get % :school-data))

(re-frame/reg-event-fx
  ::set-school-data
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data {:keys [cancel-edit-mode?] :or {cancel-edit-mode? false}}]]
    {:db       (cond-> (assoc db :school-data data)
                       cancel-edit-mode? (set-school-form-editable false))
     :dispatch [::breadcrumbs/set-current-node {:text (get data :name "")}]}))

(re-frame/reg-sub
  ::school-name
  :<- [::school-data]
  #(-> (get % :name "")
       (clojure.string/capitalize)))

;; Redirects

(re-frame/reg-event-fx
  ::open-teachers
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [school-id (:school-id db)]
      {:dispatch [::routes/redirect :teachers :school-id school-id]})))

(re-frame/reg-event-fx
  ::open-add-teacher
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [school-id (:school-id db)]
      {:dispatch [::routes/redirect :teacher-add :school-id school-id]})))

(re-frame/reg-event-fx
  ::open-students
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [school-id (:school-id db)]
      {:dispatch [::routes/redirect :students :school-id school-id]})))

(re-frame/reg-event-fx
  ::open-add-student
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [school-id (:school-id db)]
      {:dispatch [::routes/redirect :student-add :school-id school-id]})))

(re-frame/reg-event-fx
  ::open-classes
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [school-id (:school-id db)]
      {:dispatch [::routes/redirect :classes :school-id school-id]})))

(re-frame/reg-event-fx
  ::open-add-class
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [school-id (:school-id db)]
      {:dispatch [::routes/redirect :class-add :school-id school-id]})))

(re-frame/reg-event-fx
  ::open-courses
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [school-id (:school-id db)]
      {:dispatch [::routes/redirect :school-courses :school-id school-id]})))

(re-frame/reg-event-fx
  ::open-schools-list
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_]]
    {:dispatch [::routes/redirect :schools]}))
