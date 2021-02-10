(ns webchange.editor-v2.layout.flipbook.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.flipbook.utils :refer [scene-data->objects-list]]
    [webchange.editor-v2.layout.components.activity-stage.state :as stage]
    [webchange.editor-v2.layout.state :as db]
    [webchange.state.state :as state]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:stage-text-control])
       (db/path-to-db)))

(re-frame/reg-sub
  ::stage-text-data
  (fn []
    [(re-frame/subscribe [::state/scene-data])
     (re-frame/subscribe [::stage/current-stage])])
  (fn [[scene-data current-stage]]
    (scene-data->objects-list scene-data current-stage)))

(re-frame/reg-sub
  ::loading-status
  (fn [db]
    (get-in db (path-to-db [:loading-status]) :done)))

(re-frame/reg-event-fx
  ::set-loading-status
  (fn [{:keys [db]} [_ status]]
    {:db (assoc-in db (path-to-db [:loading-status]) status)}))

