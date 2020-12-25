(ns webchange.editor-v2.course-table.state.edit-common
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.events :as events]
    [webchange.warehouse :as warehouse]))

(re-frame/reg-event-fx
  ::update-course
  (fn [{:keys [_]} [_ course-slug course-data callbacks]]
    {:dispatch [::warehouse/save-course
                {:course-slug course-slug
                 :course-data course-data}
                {:on-success [::update-course-success callbacks]}]}))

(re-frame/reg-event-fx
  ::update-course-success
  (fn [{:keys [_]} [_ {:keys [on-success]} {:keys [data]}]]
    {:dispatch-n (cond-> [[::events/set-course-data data]]
                         (some? on-success) (conj on-success))}))
