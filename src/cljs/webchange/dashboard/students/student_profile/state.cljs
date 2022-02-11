(ns webchange.dashboard.students.student-profile.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.dashboard.students.events :as events]
    [webchange.dashboard.students.student-form.state :as student-form]
    [webchange.state.warehouse :as warehouse]))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_ student-id]]
    {:dispatch [::warehouse/load-student
                {:student-id student-id}
                {:on-success [::load-student-success]}]}))

(re-frame/reg-event-fx
  ::load-student-success
  (fn [{:keys [_]} [_ {:keys [student]}]]
    (let [{:keys [id class]} student
          {:keys [course-id]} class]
      {:dispatch [::warehouse/load-student-profile
                  {:student-id id
                   :course-id  course-id}
                  {:on-success [::load-profile-success]}]})))

(re-frame/reg-event-fx
  ::load-profile-success
  (fn [{:keys [_]} [_ student-profile]]
    {:dispatch [::events/set-student-profile student-profile]}))

(re-frame/reg-event-fx
  ::edit-student
  (fn [{:keys [_]} [_ student-id]]
    {:dispatch [::student-form/open-edit-student-window
                {:student-id student-id}
                {:on-success [::events/load-student student-id]}]}))
