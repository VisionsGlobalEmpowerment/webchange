(ns webchange.editor-v2.layout.components.activity-action.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.components.activity-stage.state :as stage-state]
    [webchange.editor-v2.translator.translator-form.state.form :as translator-form]
    [webchange.interpreter.events :as interpreter.events]
    [webchange.state.state :as state]
    [webchange.subs :as subs]))

(def actions-modal-state-path [:editor-v2 :translator :actions-modal-state])
(def current-action-name-path [:editor-v2 :translator :current-action-name])

(re-frame/reg-sub
  ::modal-state
  (fn [db]
    (-> db
        (get-in actions-modal-state-path)
        boolean)))

(defn- get-current-action
  [db]
  (get-in db current-action-name-path))

(re-frame/reg-sub
  ::current-action
  get-current-action)

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
    (print "::show-actions-form" action-name)
    {:dispatch-n (list [::set-current-action action-name]
                       [::open])}))

(re-frame/reg-event-fx
  ::save
  (fn [{:keys [db]} [_ data]]
    (let [current-action (get-current-action db)]
      {:dispatch [::call-activity-action
                  {:action current-action
                   :data   data}
                  {:on-success [::close]}]})))

(re-frame/reg-event-fx
  ::call-activity-action
  (fn [{:keys [db]} [_ {:keys [course-id scene-id action data] :or {data {}}} {:keys [on-success]}]]
    (let [course-id (or course-id (subs/current-course db))
          scene-id (or scene-id (subs/current-scene db))
          action (or action (get-current-action db))]
      {:dispatch [::state/call-activity-action
                  {:course-id course-id
                   :scene-id  scene-id
                   :action    action
                   :data      data}
                  {:on-success [::call-activity-action-success on-success]}]})))

(re-frame/reg-event-fx
  ::call-activity-action-success
  (fn [{:keys [_]} [_ on-success {:keys [name data]}]]
    {:pre [(string? name) (map? data)]}
    {:dispatch-n (cond-> [[::interpreter.events/set-scene name data]
                          [::interpreter.events/store-scene name data]
                          [::stage-state/reset-stage]]
                         (some? on-success) (conj on-success))}))
