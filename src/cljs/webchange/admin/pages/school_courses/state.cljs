(ns webchange.admin.pages.school-courses.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]
    [webchange.validation.specs.course-spec :as course-spec]
    [webchange.validation.validate :refer [validate]]))

(def path-to-db :school-courses)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(comment
  @(re-frame/subscribe [path-to-db])
  @(re-frame/subscribe [::course-options]))
(defn- get-school-data
  [db]
  (get db :school-data))

(defn- set-school-data
  [db school-data]
  (assoc db :school-data school-data))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [school-id]}]]
    {:db (assoc db :school-id school-id)
     :dispatch-n [[::warehouse/load-school {:school-id school-id} {:on-success [::load-school-success]}]
                  [::warehouse/load-school-courses {:school-id school-id} {:on-success [::load-school-courses-success]}]
                  [::warehouse/load-available-courses {:on-success [::load-available-courses-success]}]]}))

(re-frame/reg-event-fx
  ::load-school-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [school]}]]
    {:db (set-school-data db school)}))

(re-frame/reg-event-fx
  ::load-school-courses-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ courses]]
    {:db (assoc db :courses courses)}))

(re-frame/reg-event-fx
  ::load-available-courses-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ courses]]
    {:db (assoc db :available-courses courses)}))

(re-frame/reg-sub
  ::school-data
  :<- [path-to-db]
  get-school-data)

(re-frame/reg-sub
  ::school-name
  :<- [::school-data]
  (fn [data]
    (get data :name)))

(re-frame/reg-sub
  ::courses
  :<- [path-to-db]
  (fn [data]
    (get data :courses [])))

(re-frame/reg-sub
  ::course-options
  :<- [path-to-db]
  (fn [db]
    (->> db
         :available-courses
         (map (fn [course]
                {:value (:id course)
                 :text (:name course)})))))

(re-frame/reg-sub
  ::errors
  :<- [path-to-db]
  (fn [data]
    (get data :errors)))

(re-frame/reg-event-fx
  ::show-assign-course-modal
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (assoc db :assign-course-modal-open true)}))

(re-frame/reg-event-fx
  ::close-assign-course-modal
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (assoc db :assign-course-modal-open false)}))

(re-frame/reg-sub
  ::show-assign-course-modal?
  :<- [path-to-db]
  (fn [data]
    (get data :assign-course-modal-open false))) 

(re-frame/reg-event-fx
  ::assign-course
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data]]
    (let [school-id (:school-id db)
          validation-errors (validate ::course-spec/assign-school-course data)]
      (if (nil? validation-errors)
        {:db (assoc db :errors nil)
         :dispatch [::warehouse/assign-school-course {:school-id school-id :data data}
                    {:on-success [::assign-course-success]}]}
        {:db (assoc db :errors validation-errors)}))))

(re-frame/reg-event-fx
  ::assign-course-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [school-id (:school-id db)]
      {:db (assoc db :assign-course-modal-open false)
       :dispatch [::warehouse/load-school-courses {:school-id school-id} {:on-success [::load-school-courses-success]}]})))

(re-frame/reg-event-fx
  ::edit-course
  (fn [{:keys [db]} [_ course-id]]
    {:dispatch [::routes/redirect :edit-course :course-id course-id]}))

(re-frame/reg-event-fx
  ::remove-course
  (fn [{:keys [db]} [_ course-id]]
    (print "::remove-course" course-id)))

(re-frame/reg-event-fx
  ::copy-course
  (fn [{:keys [db]} [_ course-id]]
    (print "::copy-course" course-id)))

