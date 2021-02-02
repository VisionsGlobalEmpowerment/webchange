(ns webchange.editor-v2.scene.action.events
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.wizard.state.activity :as activity]
    [webchange.editor-v2.translator.translator-form.state.form :as translator-form]))

(def actions-modal-state-path [:editor-v2 :translator :actions-modal-state])
(def current-action-name-path [:editor-v2 :translator :current-action-name])

(re-frame/reg-sub
  ::modal-state
  (fn [db]
    (-> db
        (get-in actions-modal-state-path)
        boolean)))

(re-frame/reg-sub
  ::current-action
  (fn [db]
    (-> db
        (get-in current-action-name-path))))

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
  ::set-current-action
  (fn [{:keys [db]} [_ action-name]]
    {:db (assoc-in db current-action-name-path action-name)}))


(re-frame/reg-event-fx
  ::show-actions-form
  (fn [{:keys [_]} [_ action-name]]
    {:dispatch-n (list [::set-current-action action-name]
                       [::open])}))

(re-frame/reg-event-fx
  ::save
  (fn [{:keys [_]} [_ course-slug data scene-id callback]]
    {:dispatch-n (list [::activity/update-activity course-slug data scene-id callback])}))
