(ns webchange.student-dashboard.events
  (:require
    [re-frame.core :as re-frame]
    [webchange.events :as events]
    [webchange.interpreter.events :as ie]
    [webchange.interpreter.lessons.activity :as lessons-activity]))

(re-frame/reg-event-fx
  :open-student-dashboard
  (fn [{:keys [_]} _]
    {:dispatch-n (list [::events/redirect :student-dashboard])}))

(re-frame/reg-event-fx
  :open-student-course-dashboard
  (fn [{:keys [_]} [_ course-id]]
    {:dispatch-n (list [::events/redirect :student-course-dashboard :id course-id])}))

(re-frame/reg-event-fx
  ::show-more
  (fn [{:keys [db]} _]
    (let [course-id (:current-course db)]
      {:dispatch [::events/redirect :finished-activities :id course-id]})))

(re-frame/reg-event-fx
  ::open-activity
  (fn [{:keys [db]} [_ activity]]
    (print "::open-activity" activity)
    (let [course (:current-course db)
          activity (select-keys activity [:level :lesson :activity :activity-name])]
      (print "activity" activity)
      {:db (lessons-activity/add-loaded-activity db activity)
       :dispatch-n (list [::ie/set-current-scene (:activity-name activity)]
                         [::events/redirect (str "/courses/" course)])})))
