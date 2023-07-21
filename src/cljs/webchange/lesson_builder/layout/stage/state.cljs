(ns webchange.lesson-builder.layout.stage.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.interpreter.object-data.get-object-data :refer [get-object-data]]
    [webchange.lesson-builder.state :as state]
    [webchange.resources.scene-parser :refer [get-activity-resources]]
    [webchange.state.state :as core-state]
    [webchange.utils.flipbook :as flipbook-utils]))

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
     :metadata  (:metadata activity-data)
     :activity-data activity-data}))

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
  ::show-flipbook-actions?
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

(def stage-busy-key :stage-busy?)

(re-frame/reg-sub
  ::stage-busy?
  :<- [path-to-db]
  #(get % stage-busy-key false))

(re-frame/reg-event-fx
  ::set-stage-busy
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc db stage-busy-key value)}))

(re-frame/reg-sub
  ::current-page-side
  :<- [::state/activity-data]
  :<- [::core-state/current-object]
  (fn [[scene-data current-object] [_]]
    (when (some? current-object)
      (let [page-number (flipbook-utils/page-object-name->page-number scene-data current-object)
            stage-number (flipbook-utils/page-number->stage-number scene-data page-number)
            {:keys [pages-idx]} (flipbook-utils/get-stage-data scene-data stage-number)]
        (cond
          (= page-number (first pages-idx)) "left"
          (= page-number (last pages-idx)) "right")))))
