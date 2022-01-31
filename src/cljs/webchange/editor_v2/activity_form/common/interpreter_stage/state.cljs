(ns webchange.editor-v2.activity-form.common.interpreter-stage.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-form.common.state :as state-parent]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:interpreter-stage])
       (state-parent/path-to-db)))

(defn- generate-stage-key
  []
  (-> (random-uuid)
      (str)))

(def stage-key-path? (path-to-db [:show-stage?]))

(re-frame/reg-sub
  ::stage-key
  (fn [db]
    (get-in db stage-key-path? "default")))

(re-frame/reg-sub
  ::show-stage?
  (fn []
    (re-frame/subscribe [::stage-key]))
  (fn [stage-key]
    (some? stage-key)))

(re-frame/reg-event-fx
  ::reset-stage
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db stage-key-path? (generate-stage-key))}))

(re-frame/reg-event-fx
  ::hide-stage
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db stage-key-path? nil)}))
