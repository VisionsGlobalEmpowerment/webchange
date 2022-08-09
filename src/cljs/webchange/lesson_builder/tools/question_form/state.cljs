(ns webchange.lesson-builder.tools.question-form.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.renderer.state.scene :as state-renderer]))

(def path-to-db :lesson-builder/question-form)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-event-fx
  ::init-state
  (fn [{:keys [_]} [_ {:keys [question-id]}]]
    {:dispatch [::state-renderer/set-scene-object-state (keyword question-id) {:visible true}]}))

(re-frame/reg-event-fx
  ::reset-state
  (fn [{:keys [db]} [_ {:keys [question-id]}]]
    {:db       (dissoc db path-to-db)
     :dispatch [::state-renderer/set-scene-object-state (keyword question-id) {:visible false}]}))
