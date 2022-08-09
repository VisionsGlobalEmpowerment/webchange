(ns webchange.lesson-builder.tools.question-options.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.blocks.state :as layout-state]
    [webchange.lesson-builder.state :as state]
    [webchange.utils.scene-data :as utils]))

(re-frame/reg-sub
  ::question-options
  :<- [::state/activity-data]
  (fn [activity-data]
    (->> (utils/get-available-effects activity-data)
         (filter #(= (:type %) "question"))
         (map #(select-keys % [:name :action])))))

(re-frame/reg-event-fx
  ::edit-question
  (fn [{:keys [_]} [_ action]]
    {:dispatch [::layout-state/open-tool :question-form {:question-id action}]}))
