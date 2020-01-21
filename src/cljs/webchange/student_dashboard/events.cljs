(ns webchange.student-dashboard.events
  (:require
    [re-frame.core :as re-frame]
    [webchange.events :as events]
    [webchange.interpreter.events :as ie]))

(re-frame/reg-event-fx
  :open-student-dashboard
  (fn [{:keys [db]} _]
    {:dispatch-n (list [::events/redirect :student-dashboard])}))

(re-frame/reg-event-fx
  :open-student-course-dashboard
  (fn [{:keys [db]} [_ course-id]]
    {:dispatch-n (list [::events/redirect :student-course-dashboard :id course-id])}))

(re-frame/reg-event-fx
  ::show-more
  (fn [{:keys [db]} _]
    (let [course-id (:current-course db)]
      {:dispatch [::events/redirect :finished-activities :id course-id]})))

(re-frame/reg-event-fx
  ::open-activity
  (fn [{:keys [db]} [_ scene-name activity-id]]
    (let [course (:current-course db)]
      {:db (assoc db :loaded-activities {(keyword scene-name) activity-id})
       :dispatch-n (list [::events/redirect (str "/courses/" course)]
                         [::ie/set-current-scene scene-name])})))
