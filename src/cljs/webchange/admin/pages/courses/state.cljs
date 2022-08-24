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

(re-frame/reg-sub
  ::courses-loading?
  :<- [path-to-db]
  (fn [db]
    (or (:published-courses-loading db)
        (:my-courses-loading db))))

;; Courses Data

(re-frame/reg-sub
  ::show-my-global?
  :<- [path-to-db]
  (fn [db]
    (get db :show-my-global? true)))

(re-frame/reg-sub
  ::selected-type
  :<- [path-to-db]
  (fn [db]
    (get db :selected-type :published)))

(re-frame/reg-sub
  ::courses
  :<- [path-to-db]
  :<- [::selected-type]
  :<- [::show-my-global?]
  (fn [[db selected-type show-my-global]]
    (let [courses (if (= selected-type :published)
                    (:published-courses db)
                    (:my-courses db))]
      (if show-my-global
        courses
        (remove #(and
                   (= (:status %) "published")
                   (= (:owner-id %) (get-in db [:current-user :id])))
                courses)))))

(re-frame/reg-sub
  ::courses-counter
  :<- [path-to-db]
  (fn [db]
    {:my        (-> db :my-courses count)
     :published (-> db :published-courses count)}))
;;

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db         (-> db
                     (assoc :published-courses-loading true)
                     (assoc :my-courses-loading true))
     :dispatch-n [[::warehouse/load-available-courses
                   {:on-success [::load-courses-success]}]
                  [::warehouse/load-my-courses
                   {:on-success [::load-my-courses-success]}]
                  [::warehouse/load-current-user
                   {:on-success [::load-account-success]}]]}))

(re-frame/reg-event-fx
  ::load-account-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data]]
    {:db (-> db
             (assoc :current-user data))}))

(re-frame/reg-event-fx
  ::load-courses-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ courses]]
    {:db (-> db
             (assoc :published-courses-loading false)
             (assoc :published-courses courses))}))

(re-frame/reg-event-fx
  ::load-my-courses-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data]]
    {:db (-> db
             (assoc :my-courses-loading false)
             (assoc :my-courses data))}))

(re-frame/reg-event-fx
  ::open-course
  (fn [{:keys [_]} [_ course-slug]]
    {:dispatch [::routes/redirect :course-edit :course-slug course-slug]}))

(re-frame/reg-event-fx
  ::edit-course
  (fn [{:keys [_]} [_ course-slug]]
    {:dispatch [::routes/redirect :course-edit :course-slug course-slug
                :storage-params {:action           "edit"
                                 :on-edit-finished [::edit-course-finished]}]}))

(re-frame/reg-event-fx
  ::edit-course-finished
  (fn [{:keys [_]} [_]]
    {:dispatch [::routes/redirect :courses]}))

(re-frame/reg-event-fx
  ::duplicate-course
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_ course-id]]
    {:dispatch [::warehouse/duplicate-course {:course-id course-id} {:on-success [::duplicate-course-success]}]}))

(re-frame/reg-event-fx
  ::duplicate-course-success
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_]]
    {:dispatch-n [[::warehouse/load-available-courses
                   {:on-success [::load-courses-success]}]
                  [::warehouse/load-my-courses
                   {:on-success [::load-my-courses-success]}]]}))

(re-frame/reg-event-fx
  ::add-course
  (fn [{:keys [_]} [_]]
    {:dispatch [::routes/redirect :course-add]}))

(re-frame/reg-event-fx
  ::select-type
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ type]]
    {:db (assoc db :selected-type type)}))

(re-frame/reg-event-fx
  ::set-show-global
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc db :show-my-global? value)}))
