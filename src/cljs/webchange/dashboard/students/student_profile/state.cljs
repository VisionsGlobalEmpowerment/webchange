(ns webchange.dashboard.students.student-profile.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.dashboard.students.events :as events]
    [webchange.dashboard.students.student-form.state :as student-form]))

(re-frame/reg-event-fx
  ::edit-student
  (fn [{:keys [_]} [_ student-id]]
    {:dispatch [::student-form/open-edit-student-window
                {:student-id student-id}
                {:on-success [::events/load-student student-id]}]}))
