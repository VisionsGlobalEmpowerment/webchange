(ns webchange.scene
  (:require [webchange.demo-scenes.home :refer [home-scene]]
            [webchange.demo-scenes.map :refer [map-scene]]))

(def courses {"test" {"home" home-scene
                      "map" map-scene}})
(defn get-course
  [course-id]
  {:initial-scene "home"
   :scenes ["home" "map"]
   :lessons [{:id 1 :lesson-sets {:concepts "ls1"}}]
   :workflow-actions [{:id 1 :type "set-activity" :activity "home-introduce" :order 1}
                      {:id 2 :type "set-activity" :activity "see-saw" :order 2}
                      {:id 3 :type "set-activity" :activity "swings" :order 3}]
   :default-progress {:current-scene "home"
                      :current-activity "home-introduce"
                      :current-lesson 1
                      :sets {:concepts "ls1"}
                      :workflow-action "finish-activity"
                      :finished-workflow-actions {}}})

(defn get-scene
  [course-id scene-id]
  (get-in courses [course-id scene-id]))
