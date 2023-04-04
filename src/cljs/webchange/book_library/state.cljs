(ns webchange.book-library.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.state.warehouse :as warehouse]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:book-library])
       (vec)))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_ {:keys [course-id]}]]
    {:dispatch-n [[::set-current-course course-id]
                  [::ensure-course-info-loaded course-id]]}))

(def current-course-path (path-to-db [:current-course]))

(defn get-current-course
  [db]
  (get-in db current-course-path))

(re-frame/reg-sub
  ::current-course
  get-current-course)

(re-frame/reg-event-fx
  ::set-current-course
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db current-course-path data)}))

(re-frame/reg-event-fx
  ::ensure-course-info-loaded
  (fn [{:keys [db]} [_ course-slug]]
    (let [course-info (get-in db [:course :course-info])]
      (when-not course-info
        {:dispatch-n [[::warehouse/load-course-info course-slug
                       {:on-success [::load-course-info-success]}]]}))))

(re-frame/reg-event-fx
  ::load-course-info-success
  (fn [{:keys [db]} [_ course-info]]
    {:db (assoc-in db [:course :course-info] course-info)}))
