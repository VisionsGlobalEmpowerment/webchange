(ns webchange.lesson-builder.blocks.stage.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.object-data.get-object-data :refer [get-object-data]]
    [webchange.lesson-builder.state :as state]
    [webchange.resources.scene-parser :refer [get-activity-resources]]))

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
