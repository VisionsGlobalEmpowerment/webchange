(ns webchange.editor-v2.activity-form.common.interpreter-stage.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-form.common.state :as state-parent]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:interpreter-stage])
       (state-parent/path-to-db)))

(re-frame/reg-sub
  ::stage-key
  (fn [db]
    (get-in db (path-to-db [:stage-key]) "default")))

(re-frame/reg-event-fx
  ::reset-stage
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db (path-to-db [:stage-key]) (-> (random-uuid) str))}))