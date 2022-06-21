(ns webchange.admin.pages.courses.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :page/courses)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; Courses Loading

(def courses-loading-key :courses-loading?)

(defn- set-courses-loading
  [db value]
  (assoc db courses-loading-key value))

(re-frame/reg-sub
  ::courses-loading?
  :<- [path-to-db]
  #(get % courses-loading-key false))

;; Courses Data

(def courses-key :courses)

(defn- get-courses
  [db]
  (get db courses-key))

(defn- set-courses
  [db value]
  (assoc db courses-key value))

(re-frame/reg-sub
  ::courses
  :<- [path-to-db]
  #(get-courses %))

;;

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db       (set-courses-loading db true)
     :dispatch [::warehouse/load-available-courses
                {:on-success [::load-courses-success]}]}))

(re-frame/reg-event-fx
  ::load-courses-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ courses]]
    {:db (-> db
             (set-courses-loading false)
             (set-courses courses))}))

(re-frame/reg-event-fx
  ::edit-course
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_ course-slug]]
    {:dispatch [::routes/redirect :course-edit :course-slug course-slug]}))

(re-frame/reg-event-fx
  ::view-course
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_ slug]]
    {:dispatch [::routes/redirect :course-profile :course-slug slug]}))

(re-frame/reg-event-fx
  ::add-course
  (fn [{:keys [db]} [_]]
    {:dispatch [::routes/redirect :course-add]}))
