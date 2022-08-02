(ns webchange.lesson-builder.tools.script.dialog-item.character-movement.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.state :as lesson-builder-state]
    [webchange.lesson-builder.tools.script.dialog-item.state :as state]
    [webchange.lesson-builder.tools.script.state :as script-state]
    [webchange.lesson-builder.stage-actions :as stage-actions]
    [webchange.lesson-builder.widgets.confirm.state :as confirm-state]
    [webchange.utils.scene-action-data :as action-utils]
    [webchange.utils.animations :as animation-utils]))

(re-frame/reg-sub
  ::name
  (fn [[_ action-path]]
    [(re-frame/subscribe [::lesson-builder-state/activity-data])
     (re-frame/subscribe [::state/action-data action-path])])
  (fn [[activity-data action-data]]
    (let [{:keys [action target transition-id]} (action-utils/get-inner-action action-data)]
      (animation-utils/action->display-name {:activity-data activity-data
                                             :character     transition-id
                                             :target        target
                                             :action        action}))))

;; remove

(re-frame/reg-event-fx
  ::remove
  (fn [_ [_ action-path]]
    {:dispatch [::confirm-state/show-confirm-window {:title      "Are you sure you want to delete this movement?"
                                                     :on-confirm [::stage-actions/remove-action {:action-path action-path}]}]}))
