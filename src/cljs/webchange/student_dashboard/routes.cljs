(ns webchange.student-dashboard.routes
  (:require
    [re-frame.core :as re-frame]
    [webchange.events :as events]))

(def routes {[""] :student-course-dashboard})

(re-frame/reg-event-fx
  ::open-student-course-dashboard
  (fn [{:keys [_]} [_ {:keys [course-id]}]]
    {:dispatch-n (list [::events/redirect :student-course-dashboard :id course-id])}))
