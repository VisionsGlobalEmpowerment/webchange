(ns webchange.lesson-builder.tools.script.dialog-item.question.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.state :as lesson-builder-state]
    [webchange.lesson-builder.tools.script.dialog-item.state :as state]
    [webchange.lesson-builder.tools.script.state :as script-state]
    [webchange.lesson-builder.stage-actions :as stage-actions]
    [webchange.utils.scene-data :as utils]
    [webchange.utils.scene-action-data :as action-utils]))

(re-frame/reg-sub
  ::question-options
  :<- [::lesson-builder-state/activity-data]
  (fn [activity-data]
    (->> (utils/get-available-effects activity-data)
         (filter #(= (:type %) "question"))
         (map #(select-keys % [:name :action])))))

(re-frame/reg-sub
  ::name
  (fn [[_ action-path]]
    [(re-frame/subscribe [::state/action-data action-path])
     (re-frame/subscribe [::question-options])])
  (fn [[action-data question-options]]
    (let [{:keys [id]} (action-utils/get-inner-action action-data)]
      (some (fn [{:keys [name action]}]
              (and (= action id) name))
            question-options))))

;; remove

(re-frame/reg-event-fx
  ::remove
  (fn [_ [_ action-path]]
    {:dispatch [::script-state/show-confirm-window {:text       "Are you sure you want to delete this question?"
                                                    :on-confirm [::stage-actions/remove-action {:action-path action-path}]}]}))
