(ns webchange.dashboard.classes.class-profile.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.dashboard.classes.class-form.state :as class-form]
    [webchange.dashboard.classes.state :as parent-state]
    [webchange.dashboard.events :as dashboard-events]
    [webchange.logger.index :as logger]
    [webchange.state.warehouse :as warehouse]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:class-profile])
       (parent-state/path-to-db)))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_ {:keys [class-id]}]]
    (logger/trace-folded-defs "Init class profile" "class-id" class-id)
    {:dispatch [::load-class class-id]}))

(re-frame/reg-event-fx
  ::load-class
  (fn [{:keys [_]} [_ class-id]]
    {:dispatch [::warehouse/load-class class-id {:on-success [::load-class-success]}]}))

(re-frame/reg-event-fx
  ::load-class-success
  (fn [{:keys [_]} [_ response]]
    (let [{:keys [course-slug] :as class-data} (:class response)]
      {:dispatch-n [[::set-class-data class-data]
                    [::load-course course-slug]]})))

(re-frame/reg-event-fx
  ::load-course
  (fn [{:keys [_]} [_ course-id]]
    {:dispatch [::warehouse/load-course-info course-id {:on-success [::load-course-success]}]}))

(re-frame/reg-event-fx
  ::load-course-success
  (fn [{:keys [_]} [_ response]]
    {:dispatch [::set-course-data response]}))

;; Class

(def class-data-path (path-to-db [:class-data]))

(defn get-class-data
  [db]
  (get-in db class-data-path))

(re-frame/reg-sub
  ::class-data
  get-class-data)

(re-frame/reg-sub
  ::class-name
  (fn []
    [(re-frame/subscribe [::class-data])])
  (fn [[class-data]]
    (get class-data :name)))

(defn get-class-id
  [db]
  (-> (get-class-data db)
      (get :id)))


(re-frame/reg-event-fx
  ::set-class-data
  (fn [{:keys [db]} [_ class-data]]
    (logger/trace-folded "Class data" class-data)
    {:db (assoc-in db class-data-path class-data)}))

;; Course

(def course-data-path (path-to-db [:course-data]))

(re-frame/reg-event-fx
  ::set-course-data
  (fn [{:keys [db]} [_ course-data]]
    (logger/trace-folded "Course data" course-data)
    {:db (assoc-in db course-data-path course-data)}))

(defn get-course-data
  [db]
  (get-in db course-data-path))

(re-frame/reg-sub
  ::course-data
  get-course-data)

(re-frame/reg-sub
  ::course-name
  (fn []
    [(re-frame/subscribe [::course-data])])
  (fn [[course-data]]
    (get course-data :name)))

(re-frame/reg-sub
  ::course-slug
  (fn []
    [(re-frame/subscribe [::course-data])])
  (fn [[course-data]]
    (get course-data :slug)))

(re-frame/reg-event-fx
  ::open-students-page
  (fn [{:keys [db]} [_]]
    (let [class-id (get-class-id db)]
      {:redirect [:dashboard-students :class-id class-id]})))

(re-frame/reg-event-fx
  ::open-edit-class-form
  (fn [{:keys [db]} [_]]
    (let [class-id (get-class-id db)]
      {:dispatch [::class-form/open-edit-class-window class-id {:on-success [::init {:class-id class-id}]}]})))

(re-frame/reg-event-fx
  ::open-remove-class-form
  (fn [{:keys [db]} [_]]
    (let [class-id (get-class-id db)]
      {:dispatch [::dashboard-events/show-delete-class-form class-id]})))
