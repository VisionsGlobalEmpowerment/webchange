(ns webchange.editor-v2.layout.flipbook.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.flipbook.utils :refer [get-stage-data]]
    [webchange.editor-v2.layout.components.activity-stage.state :as stage]
    [webchange.state.state :as state]))

(defn- filter-actions
  [actions-data stage-actions]
  (->> actions-data
       (filter (fn [[action-name]]
                 (some #{action-name} stage-actions)))
       (into {})))

(defn- get-stage-actions
  [scene-data current-stage]
  (if (some? current-stage)
    (let [stage (get-stage-data scene-data current-stage)
          actions (->> [(:left-page stage) (:right-page stage)]
                       (map :action)
                       (remove nil?)
                       (map keyword))]
      (-> scene-data
          (update :actions filter-actions actions)))
    scene-data))

(re-frame/reg-sub
  ::stage-data
  (fn []
    [(re-frame/subscribe [::state/scene-data])
     (re-frame/subscribe [::stage/current-stage])])
  (fn [[scene-data current-stage]]
    (get-stage-actions scene-data current-stage)))
