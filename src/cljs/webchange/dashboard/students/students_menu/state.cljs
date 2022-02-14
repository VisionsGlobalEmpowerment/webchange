(ns webchange.dashboard.students.students-menu.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.dashboard.students.events :as events]
    [webchange.dashboard.students.student-form.state :as student-form]))

(re-frame/reg-event-fx
  ::add-student
  (fn [{:keys [_]} [_ class-id]]
    {:dispatch [::student-form/open-add-student-window
                {:class-id class-id}
                {:on-success [::events/load-students class-id]}]}))

(re-frame/reg-event-fx
  ::edit-student
  (fn [{:keys [_]} [_ student-id class-id]]
    {:dispatch [::student-form/open-edit-student-window
                {:student-id student-id}
                {:on-success [::events/load-students class-id]}]}))