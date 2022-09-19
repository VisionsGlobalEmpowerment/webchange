(ns webchange.lesson-builder.tools.voice-translate.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.stage-actions :as stage-actions]
    [webchange.lesson-builder.state :as state]
    [webchange.lesson-builder.tools.script.state :as script-state]
    [webchange.utils.scene-action-data :as action-utils]
    [webchange.utils.scene-data :as activity-utils]))

(def path-to-db :lesson-builder/voice-translate)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-sub
  ::action-data
  :<- [::state/activity-data]
  :<- [::script-state/selected-action]
  (fn [[activity-data action-path]]
    (activity-utils/get-action activity-data action-path)))

(re-frame/reg-sub
  ::selected-audio
  :<- [::action-data]
  (fn [action-data]
    (-> (action-utils/get-inner-action action-data)
        (get :audio))))

(re-frame/reg-sub
  ::file-name
  :<- [::state/activity-data]
  :<- [::selected-audio]
  (fn [[activity-data selected-audio]]
    (->> (activity-utils/find-asset activity-data (fn [{:keys [data]}]
                                                    (= (:url data) selected-audio)))
         (activity-utils/asset-data->asset-name))))

(re-frame/reg-event-fx
  ::set-selected-audio
  (fn [{:keys [db]} [_ action-path value]]
    {:dispatch [::stage-actions/set-action-phrase-audio {:action-path action-path
                                                         :audio-url value}]}))

(re-frame/reg-sub
  ::selected-action
  :<- [::script-state/selected-action]
  identity)

(re-frame/reg-sub
  ::show-audio-editor?
  :<- [::selected-audio]
  #(some? %))

(re-frame/reg-event-fx
  ::apply
  (fn [{:keys [db]} [_]]
    {:dispatch-n [[::state/save-activity]
                  [:layout/reset]]}))

(re-frame/reg-event-fx
  ::set-apply-disabled
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc db :apply-disabled? value)}))

(re-frame/reg-sub
  ::apply-disabled?
  :<- [path-to-db]
  (fn [db]
    (get db :apply-disabled? false)))
