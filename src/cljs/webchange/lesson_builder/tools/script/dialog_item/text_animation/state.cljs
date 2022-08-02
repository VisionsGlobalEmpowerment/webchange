(ns webchange.lesson-builder.tools.script.dialog-item.text-animation.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.state :as lesson-builder]
    [webchange.lesson-builder.tools.script.dialog-item.state :as state]
    [webchange.lesson-builder.tools.script.state :as script-state]
    [webchange.lesson-builder.stage-actions :as stage-actions]
    [webchange.lesson-builder.widgets.confirm.state :as confirm-state]
    [webchange.utils.scene-action-data :as action-utils]
    [webchange.utils.scene-data :as activity-utils]))

;; target

(re-frame/reg-sub
  ::target
  (fn [[_ action-path]]
    (re-frame/subscribe [::state/action-data action-path]))
  (fn [action-data]
    (let [inner-action (action-utils/get-inner-action action-data)]
      (get inner-action :target))))

(re-frame/reg-event-fx
  ::set-target
  (fn [_ [_ action-path target]]
    {:dispatch [::stage-actions/set-action-target {:action-path action-path
                                                   :target      target}]}))

;; phrase text

(re-frame/reg-sub
  ::text
  (fn [[_ action-path]]
    [(re-frame/subscribe [::lesson-builder/activity-data])
     (re-frame/subscribe [::state/action-data action-path])])
  (fn [[activity-data action-data]]
    (let [{:keys [target]} (action-utils/get-inner-action action-data)]
      (-> (activity-utils/get-scene-object activity-data target)
          (get :text)))))

(re-frame/reg-event-fx
  ::set-text
  [(re-frame/inject-cofx :activity-data)]
  (fn [{:keys [activity-data]} [_ action-path text]]
    (let [{:keys [target]} (-> (activity-utils/get-action activity-data action-path)
                               (action-utils/get-inner-action))]
      {:dispatch [::stage-actions/set-object-text {:object-name target
                                                   :text        text}]})))

;; remove

(re-frame/reg-event-fx
  ::remove
  (fn [_ [_ action-path]]
    {:dispatch [::confirm-state/show-confirm-window {:title      "Are you sure you want to delete this text animation action?"
                                                     :on-confirm [::stage-actions/remove-action {:action-path action-path}]}]}))
