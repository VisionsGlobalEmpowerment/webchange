(ns webchange.scene
  (:require [webchange.demo-scenes.home :refer [home-scene]]
            [webchange.demo-scenes.map :refer [map-scene]]
            [webchange.demo-scenes.park.see-saw :refer [see-saw-scene]]
            [webchange.demo-scenes.park.swings :refer [swings-scene]]
            [webchange.demo-scenes.park.sandbox :refer [sandbox-scene]]
            [webchange.demo-scenes.park.park :refer [park-scene]]))

(def courses {"test" {"home" home-scene
                      "map" map-scene
                      "see-saw" see-saw-scene
                      "swings" swings-scene
                      "sandbox" sandbox-scene
                      "park" park-scene}})
(defn get-course
  [course-id]
  {:initial-scene "map"
   :scenes ["home" "map" "see-saw" "swings" "sandbox" "park"]
   :lessons [{:id 1 :lesson-sets {:concepts "ls1"}}]
   :workflow-actions [{:id 1 :type "set-activity" :activity "home-introduce" :order 1}
                      {:id 2 :type "set-activity" :activity "see-saw" :order 2}
                      {:id 3 :type "set-activity" :activity "swings" :order 3}
                      {:id 3 :type "set-activity" :activity "sandbox" :order 4}]
   :default-progress {:current-scene "home"
                      :current-activity "home"
                      :variables {:last-location "home"}
                      :current-lesson 1
                      :sets {:concepts "ls1"}
                      :workflow-action "finish-activity"
                      :finished-workflow-actions {}}})

(defn get-scene
  [course-id scene-id]
  (get-in courses [course-id scene-id]))
