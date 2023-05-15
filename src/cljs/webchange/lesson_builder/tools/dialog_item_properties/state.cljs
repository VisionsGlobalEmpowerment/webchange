(ns webchange.lesson-builder.tools.dialog-item-properties.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.stage-actions :as stage-actions]
    [webchange.lesson-builder.state :as state]
    [webchange.lesson-builder.tools.script.state :as script-state]
    [webchange.utils.scene-action-data :as action-utils]
    [webchange.utils.scene-data :as activity-utils]))

(re-frame/reg-sub
  ::selected-action
  :<- [::script-state/selected-action]
  identity)

(re-frame/reg-event-fx
  ::apply
  (fn [{:keys [_db]} [_]]
    {:dispatch-n [[::state/save-activity]
                  [:layout/reset]]}))

(re-frame/reg-sub
  ::delay
  :<- [::state/activity-data]
  :<- [::script-state/selected-action]
  (fn [[activity-data action-path]]
    (-> (activity-utils/get-action activity-data action-path)
        (get-in action-utils/empty-action-path)
        (get :duration))))

(re-frame/reg-event-fx
  ::set-delay
  (fn [{:keys [_db]} [_ action-path value]]
    {:dispatch [::stage-actions/update-empty-action {:action-path action-path
                                                     :data-patch  {:duration value}}]}))
