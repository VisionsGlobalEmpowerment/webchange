(ns webchange.lesson-builder.tools.select-view.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.state :as state]
    [webchange.interpreter.renderer.state.scene :as renderer-state]))

(def path-to-db :lesson-builder/select-view)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(defn get-views
  [activity-data]
  (->> activity-data :views))

(re-frame/reg-sub
  ::available-views
  :<- [::state/activity-data]
  (fn [activity-data]
    (get-views activity-data)))

(re-frame/reg-event-fx
  ::select-view
  [(i/path path-to-db)]
  (fn [{:keys [_db]} [_ view-id]]
    {:dispatch [::renderer-state/set-scene-view view-id]}))

(comment
  (-> @re-frame.db/app-db
      keys
      )

  @(re-frame/subscribe [::state/activity-data])
  @(re-frame/subscribe [::available-views]))
