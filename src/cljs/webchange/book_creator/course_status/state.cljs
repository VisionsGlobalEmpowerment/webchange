(ns webchange.book-creator.course-status.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.state.state-course :as state-course]))

(re-frame/reg-sub
  ::status
  (fn []
    [(re-frame/subscribe [::state-course/course-info])])
  (fn [[course-data]]
    (get course-data :status)))

(re-frame/reg-event-fx
  ::publish
  (fn [{:keys [_]} [_]]
    {:dispatch [::state-course/publish-course]}))
