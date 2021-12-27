(ns webchange.student-dashboard-v2.toolbar.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.events :as events]))

(re-frame/reg-event-fx
  ::open-page
  (fn [{:keys [_]} [_ page-id]]
    (print "::open-page" page-id)
    {:dispatch [::events/redirect :student-dashboard]}))
