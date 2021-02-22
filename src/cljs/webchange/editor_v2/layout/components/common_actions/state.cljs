(ns webchange.editor-v2.layout.components.common_actions.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.state.core :as core]
    [webchange.editor-v2.layout.components.activity-stage.state :as stage-state]
    [webchange.interpreter.events :as interpreter.events]
    [webchange.state.warehouse :as warehouse]))

(re-frame/reg-event-fx
  ::save
  (fn [{:keys [db]} [_ type data on-success]]
    (let []
      {:dispatch [::call-activity-common-action
                  {:action type
                   :data   data}
                  {:on-success on-success}]})))

(re-frame/reg-event-fx
  ::call-activity-common-action
  (fn [{:keys [db]} [_ {:keys [action data] :or {data {}}} {:keys [on-success]}]]
    {:pre [(some? action)]}
    (let [course-id  (core/current-course-id db)
          scene-id  (core/current-scene-id db)]
      {:dispatch [::warehouse/update-activity
                  {:course-id course-id
                   :scene-id  scene-id
                   :data      {:common-action? true
                               :action action
                               :data   data}}
                  {:on-success [::call-activity-action-success on-success]}]})))



(re-frame/reg-event-fx
  ::call-activity-action-success
  (fn [{:keys [_]} [_ on-success {:keys [name data]}]]
    {:pre [(string? name) (map? data)]}
    {:dispatch-n (cond-> [[::interpreter.events/set-scene name data]
                          [::interpreter.events/store-scene name data]
                          [::stage-state/reset-stage]]
                         (some? on-success) (conj on-success))}))
