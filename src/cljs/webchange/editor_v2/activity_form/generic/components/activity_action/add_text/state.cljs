(ns webchange.editor-v2.activity-form.generic.components.activity-action.add-text.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-form.generic.components.activity-action.state :as parent-state]
    [webchange.state.state-flipbook :as state-flipbook]
    [webchange.logger.index :as logger]))

(re-frame/reg-event-fx
  ::call-action
  (fn [{:keys [_]} [_ action-name]]
    {:dispatch [::parent-state/show-actions-form action-name {:on-save ::save}]}))

(re-frame/reg-event-fx
  ::save
  (fn [{:keys [db]} [_ {:keys [action data]}]]
    (let [current-page-number (state-flipbook/get-current-page-number db)]
      (if (number? current-page-number)
        {:dispatch [::parent-state/save {:action action
                                         :data   (merge data
                                                        {:page-number current-page-number})}]}
        (do (logger/error (str "Current page in not a number: " current-page-number))
            {})))))
