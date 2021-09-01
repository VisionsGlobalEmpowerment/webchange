(ns webchange.editor-v2.activity-dialogs.menu.sections.delay.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.dialog.dialog-form.state.actions :as state-actions]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.state :as parent-state]
    [webchange.editor-v2.activity-dialogs.form.state :as state-dialog]
    [webchange.editor-v2.dialog.utils.dialog-action :refer [get-empty-action]]))

(re-frame/reg-sub
  ::current-value
  (fn []
    [(re-frame/subscribe [::parent-state/selected-action-data])])
  (fn [[selected-action-data]]
    (-> (get-empty-action selected-action-data)
        (get :duration))))

(re-frame/reg-event-fx
  ::set-delay
  (fn [{:keys [db]} [_ value]]
    (let [{:keys [path source]} (state-dialog/get-selected-action db)]
      {:dispatch [::state-actions/update-empty-action-by-path {:action-path path
                                                               :action-type source
                                                               :data-patch  {:duration (float value)}}]})))
