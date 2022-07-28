(ns webchange.lesson-builder.tools.script.dialog-item.character-animation.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.state :as lesson-builder-state]
    [webchange.lesson-builder.tools.script.dialog-item.state :as state]
    [webchange.lesson-builder.tools.script.state :as script-state]
    [webchange.lesson-builder.stage-actions :as stage-actions]
    [webchange.utils.scene-action-data :as action-utils]
    [webchange.utils.scene-data :as utils]))

(re-frame/reg-sub
  ::name
  (fn [[_ action-path]]
    [(re-frame/subscribe [::lesson-builder-state/activity-data])
     (re-frame/subscribe [::state/action-data action-path])])
  (fn [[activity-data action-data]]
    (let [{:keys [id target type]} (action-utils/get-inner-action action-data)
          {:keys [scene-name]} (utils/get-scene-object activity-data target)

          target-name (-> (or scene-name target)
                          (clojure.string/replace "-" " ")
                          (clojure.string/capitalize))
          emotion-name (-> (action-utils/animation->display-name (or id type))
                           (clojure.string/capitalize))]
      (str target-name ": " emotion-name))))

;; remove

(re-frame/reg-event-fx
  ::remove
  (fn [_ [_ action-path]]
    {:dispatch [::script-state/show-confirm-window {:text       "Are you sure you want to delete this animation?"
                                                    :on-confirm [::stage-actions/remove-action {:action-path action-path}]}]}))
