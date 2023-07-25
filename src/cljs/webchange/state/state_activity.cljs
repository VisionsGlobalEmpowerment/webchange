(ns webchange.state.state-activity
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.events :as interpreter.events]
    [webchange.state.core :as core]
    [webchange.state.state :as state]
    [webchange.state.warehouse :as warehouse]))

(def current-action-name-path [:editor-v2 :translator :current-action-name])

(defn get-current-action
  [db]
  (get-in db current-action-name-path))

(re-frame/reg-sub
  ::current-action
  get-current-action)

(re-frame/reg-event-fx
  ::set-current-action
  (fn [{:keys [db]} [_ action-name]]
    {:db (assoc-in db current-action-name-path action-name)}))

(re-frame/reg-event-fx
  ::call-activity-action
  (fn [{:keys [db]} [_ {:keys [course-id scene-id action data] :or {data {}}} {:keys [on-success]}]]
    (let [course-id (or course-id (core/current-course-id db))
          scene-id (or scene-id (core/current-scene-id db))
          action (or action (get-current-action db))]
      {:dispatch [::warehouse/update-activity
                  {:course-slug course-id
                   :scene-slug  scene-id
                   :data        {:action action
                                 :data   data}}
                  {:on-success [::call-activity-action-success on-success scene-id]}]})))

(re-frame/reg-event-fx
  ::call-activity-common-action
  (fn [{:keys [db]} [_ {:keys [action data] :or {data {}}} {:keys [on-success]}]]
    {:pre [(some? action)]}
    (let [course-id (core/current-course-id db)
          scene-id (core/current-scene-id db)]
      {:dispatch [::warehouse/update-activity
                  {:course-slug course-id
                   :scene-slug  scene-id
                   :data        {:common-action? true
                                 :action         action
                                 :data           data}}
                  {:on-success [::call-activity-action-success on-success scene-id]}]})))

(re-frame/reg-event-fx
  ::call-activity-action-success
  (fn [{:keys [_]} [_ on-success scene-id {:keys [data]}]]
    {:pre [(map? data)]}
    {:dispatch-n (cond-> [[::core/set-scene-data {:scene-id   scene-id
                                                  :scene-data data}]
                          [::interpreter.events/set-scene scene-id data]
                          [::interpreter.events/store-scene scene-id data]
                          [::state/update-last-saved]]
                         (some? on-success) (conj on-success))}))

(re-frame/reg-event-fx
  ::create-activity
  (fn [{:keys [_]} [_ {:keys [course-slug activity-data]} handlers]]
    {:dispatch [::warehouse/create-activity {:course-slug   course-slug
                                             :activity-data activity-data} handlers]}))
