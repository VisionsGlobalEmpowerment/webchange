(ns webchange.lesson-builder.tools.script.dialog-item.effect-general.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.script.dialog-item.state :as state]
    [webchange.lesson-builder.tools.script.state :as script-state]
    [webchange.lesson-builder.tools.stage-actions :as stage-actions]
    [webchange.utils.scene-action-data :as action-utils]))

(re-frame/reg-sub
  ::name
  (fn [[_ action-path]]
    (re-frame/subscribe [::state/action-data action-path]))
  (fn [action-data]
    (let [inner-action (action-utils/get-inner-action action-data)]
      (-> (or (get inner-action :id)
              (get inner-action :type))
          (clojure.string/replace "-" " ")
          (clojure.string/capitalize)))))

;; remove

(re-frame/reg-event-fx
  ::remove
  (fn [_ [_ action-path]]
    {:dispatch [::script-state/show-confirm-window {:text       "Are you sure you want to delete this effect action?"
                                                    :on-confirm [::stage-actions/remove-action {:action-path action-path}]}]}))
