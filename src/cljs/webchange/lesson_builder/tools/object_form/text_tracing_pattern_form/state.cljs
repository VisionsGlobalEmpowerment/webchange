(ns webchange.lesson-builder.tools.object-form.text-tracing-pattern-form.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.interpreter.renderer.state.scene :as state-renderer]
    [webchange.lesson-builder.tools.object-form.state :as object-form-state :refer [path-to-db]]))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ object-name]]
    {}))

(re-frame/reg-sub
  ::dashed
  :<- [path-to-db]
  (fn [db [_ object-name]]
    (get-in db [:objects object-name :dashed])))

(re-frame/reg-event-fx
  ::set-dashed
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ object-name value]]
    {:db (assoc-in db [:objects object-name :dashed] value)
     :dispatch [::state-renderer/set-scene-object-state object-name {:dashed value}]}))

(re-frame/reg-sub
  ::show-lines
  :<- [path-to-db]
  (fn [db [_ object-name]]
    (get-in db [:objects object-name :show-lines])))

(re-frame/reg-event-fx
  ::set-show-lines
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ object-name value]]
    {:db (assoc-in db [:objects object-name :show-lines] value)
     :dispatch [::state-renderer/set-scene-object-state object-name {:show-line value}]}))
