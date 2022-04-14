(ns webchange.state.state-course
  (:require
    [re-frame.core :as re-frame]
    [webchange.progress.finish :as progress-utils]
    [webchange.state.core :as core]
    [webchange.state.warehouse :as warehouse]))

(defn path-to-db [relative-path] (concat [:course] relative-path))

;; Create

(re-frame/reg-event-fx
  ::create-course
  (fn [{:keys [_]} [_ {:keys [course-data]} handlers]]
    {:dispatch [::warehouse/create-course {:course-data course-data} handlers]}))

;; Course Data

(def course-data-path [:course-data])

(defn get-course-data
  [db]
  (get-in db course-data-path))

(re-frame/reg-sub
  ::course-data
  (fn [db]
    (get-in db course-data-path {})))

(re-frame/reg-event-fx
  ::set-course-data
  (fn [{:keys [db]} [_ course-data]]
    {:db (-> db
             (assoc-in course-data-path course-data)
             (assoc :course-data course-data))}))

(re-frame/reg-event-fx
  ::load-course-data
  (fn [{:keys [_]} [_ course-slug]]
    {:dispatch [::warehouse/load-course course-slug {:on-success [::set-course-data]}]}))

;; Course Info

(def course-info-path (path-to-db [:course-info]))

(defn get-course-info
  [db]
  (get-in db course-info-path {}))

(re-frame/reg-sub
  ::course-info
  get-course-info)

(re-frame/reg-event-fx
  ::load-course-info
  (fn [{:keys [_]} [_ course-slug]]
    {:dispatch [::warehouse/load-course-info course-slug {:on-success [::set-course-info]}]}))

(re-frame/reg-event-fx
  ::set-course-info
  (fn [{:keys [db]} [_ course-info]]
    {:db (assoc-in db course-info-path course-info)}))

(re-frame/reg-event-fx
  ::set-course-status
  (fn [{:keys [db]} [_ status]]
    {:db (assoc-in db (concat course-info-path [:status]) status)}))

;; Course  status

(re-frame/reg-event-fx
  ::publish-course
  (fn [{:keys [db]} [_ course-slug]]
    (let [course-slug (or course-slug (core/current-course-id db))]
      {:dispatch [::warehouse/publish-course course-slug {:on-success [::set-course-info]}]})))

;; Progress

(defn- get-progress-data
  [db]
  (get-in db [:progress-data]))

(re-frame/reg-sub
  ::progress-data
  get-progress-data)

(defn course-finished?
  ([db]
   (course-finished? (get-course-data db)
                     (get-progress-data db)))
  ([course-data progress-data]
   (progress-utils/course-finished? course-data (:finished progress-data))))

(re-frame/reg-sub
  ::course-finished?
  (fn []
    [(re-frame/subscribe [::course-data])
     (re-frame/subscribe [::progress-data])])
  (fn [[course-data progress-data]]
    (course-finished? course-data progress-data)))
