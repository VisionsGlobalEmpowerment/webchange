(ns webchange.editor-v2.layout.components.activity-action.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.state.form :as translator-form]
    [webchange.state.state-activity :as state-activity]))

(def actions-modal-state-path [:editor-v2 :translator :actions-modal-state])

(re-frame/reg-sub
  ::modal-state
  (fn [db]
    (-> db
        (get-in actions-modal-state-path)
        boolean)))

(re-frame/reg-event-fx
  ::open
  (fn [{:keys [db]} [_]]
    {:db       (assoc-in db actions-modal-state-path true)
     :dispatch [::translator-form/init-state]}))

(re-frame/reg-event-fx
  ::close
  (fn [{:keys [db]} [_]]
    {:db       (assoc-in db actions-modal-state-path false)
     :dispatch [::translator-form/reset-state]}))

(re-frame/reg-event-fx
  ::show-actions-form
  (fn [{:keys [_]} [_ action-name]]
    {:dispatch-n (list [::state-activity/set-current-action action-name]
                       [::open])}))

(re-frame/reg-event-fx
  ::save
  (fn [{:keys [db]} [_ data]]
    (let [current-action (state-activity/get-current-action db)]
      {:dispatch [::state-activity/call-activity-action
                  {:action current-action
                   :data   data}
                  {:on-success [::close]}]})))
