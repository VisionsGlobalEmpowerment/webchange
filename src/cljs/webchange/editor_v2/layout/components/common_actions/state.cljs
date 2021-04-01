(ns webchange.editor-v2.layout.components.common_actions.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.state.state-activity :as state-activity]))

(re-frame/reg-event-fx
  ::save
  (fn [{:keys []} [_ type data on-success]]
    (let []
      {:dispatch [::state-activity/call-activity-common-action
                  {:action type
                   :data   data}
                  {:on-success on-success}]})))
