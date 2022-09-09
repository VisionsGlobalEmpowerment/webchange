(ns webchange.lesson-builder.blocks.stage.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.interpreter.object-data.get-object-data :refer [get-object-data]]
    [webchange.lesson-builder.state :as state]
    [webchange.resources.scene-parser :refer [get-activity-resources]]))

(def path-to-db :lesson-builder/stage)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(defn- get-scene-objects
  [{:keys [scene-objects objects metadata]}]
  (->> (flatten scene-objects)
       (map #(get-object-data nil % objects metadata))))

(re-frame/reg-sub
  ::scene-data
  :<- [::state/activity-info]
  :<- [::state/activity-data]
  (fn [[activity-info activity-data]]
    {:scene-id  (:id activity-info)
     :objects   (get-scene-objects activity-data)
     :resources (get-activity-resources activity-data)
     :metadata  (:metadata activity-data)}))

(re-frame/reg-sub
  ::stage-key
  :<- [path-to-db]
  (fn [db]
    (get db :stage-key "default")))

(re-frame/reg-event-fx
  ::reset
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (assoc db :stage-key (rand))}))

(re-frame/reg-event-fx
  :stage/reset
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (assoc db :stage-key (rand))}))

(re-frame/reg-sub
  ::show-bottom-actions?
  :<- [::state/flipbook?]
  identity)

(def stage-ready-key :stage-ready?)

(re-frame/reg-sub
  ::stage-ready?
  :<- [path-to-db]
  #(get % stage-ready-key false))

(re-frame/reg-event-fx
  ::set-stage-ready
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc db stage-ready-key value)}))
