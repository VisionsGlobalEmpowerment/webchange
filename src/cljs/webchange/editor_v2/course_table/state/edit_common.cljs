(ns webchange.editor-v2.course-table.state.edit-common
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.events :as events]
    [webchange.warehouse :as warehouse]))

(re-frame/reg-event-fx
  ::update-course
  (fn [{:keys [_]} [_ course-id course-data]]
    {:dispatch [::warehouse/save-course
                {:course-id   course-id
                 :course-data course-data}
                {:on-success [::update-course-success]}]}))

(re-frame/reg-event-fx
  ::update-course-success
  (fn [{:keys [_]} [_ {:keys [data]}]]
    {:dispatch [::events/set-course-data data]}))
