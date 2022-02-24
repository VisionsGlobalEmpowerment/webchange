(ns webchange.book-library.state
  (:require
    [re-frame.core :as re-frame]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:book-library])
       (vec)))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_ {:keys [course-id]}]]
    {:dispatch [::set-current-course course-id]}))

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
