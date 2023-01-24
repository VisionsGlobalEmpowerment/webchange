(ns webchange.lesson-builder.tools.script.dialog.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.state :as state]
    [webchange.lesson-builder.stage-actions :as stage-actions]
    [webchange.utils.scene-action-data :as action-data-utils]
    [webchange.utils.scene-data :as utils]))

(re-frame/reg-sub
  ::dialog-name
  :<- [::state/activity-data]
  (fn [activity-data [_ dialog-action-path]]
    (-> (utils/get-action activity-data dialog-action-path)
        (get :phrase-description "Untitled Dialog"))))

(re-frame/reg-sub
  ::dialog-items
  :<- [::state/activity-data]
  (fn [activity-data [_ dialog-action-path]]
    (->> (utils/get-action activity-data dialog-action-path)
         (:data)
         (map-indexed (fn [idx {:keys [uid]}]
                        {:id          uid
                         :action-path (-> (concat dialog-action-path [:data idx]))})))))

(re-frame/reg-sub
  ::user-interactions-blocked?
  :<- [::state/activity-data]
  (fn [activity-data [_ dialog-action-path]]
    (->> (utils/get-action activity-data dialog-action-path)
         (:tags)
         (some #{(:user-interactions-blocked action-data-utils/action-tags)})
         (boolean))))

(re-frame/reg-event-fx
  ::toggle-user-interactions-block
  (fn [_ [_ dialog-action-path]]
    {:dispatch [::stage-actions/toggle-action-tag {:action-path dialog-action-path
                                                   :tag         (:user-interactions-blocked action-data-utils/action-tags)}]}))
(re-frame/reg-sub
  ::collapse-state
  :<- [::state/flipbook?]
  (fn [flipbook? [_]]
    (if flipbook?
      :expanded
      nil)))

(re-frame/reg-event-fx
  ::add-default-phrase
  (fn [_ [_ action-path]]
    {:dispatch [::stage-actions/insert-action {:action-data      (action-data-utils/create-dialog-animation-sequence-action)
                                               :parent-data-path (concat action-path [:data])
                                               :position         0}]}))
