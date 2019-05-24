(ns webchange.scene
  (:require [webchange.demo-scenes.home :refer [home-scene]]
            [webchange.demo-scenes.map :refer [map-scene]]
            [webchange.demo-scenes.park.see-saw :refer [see-saw-scene]]
            [webchange.demo-scenes.park.swings :refer [swings-scene]]
            [webchange.demo-scenes.park.sandbox :refer [sandbox-scene]]
            [webchange.demo-scenes.park.park :refer [park-scene]]
            [webchange.demo-scenes.stadium.volleyball :refer [volleyball-scene]]
            [webchange.demo-scenes.park.hide-n-seek :refer [hide-n-seek-scene]]))

(def courses {"test" {"home" home-scene
                      "map" map-scene
                      "see-saw" see-saw-scene
                      "swings" swings-scene
                      "sandbox" sandbox-scene
                      "park" park-scene
                      "volleyball" volleyball-scene
                      "hide-n-seek" hide-n-seek-scene}})
(defn get-course
  [course-id]
  {:initial-scene "hide-n-seek"
   :scenes ["home" "map" "see-saw" "swings" "sandbox" "park" "volleyball" "hide-n-seek"]
   :lessons [{:id 1 :lesson-sets {:concepts "ls1"
                                  :assessment-1 "assessment1"}}]
   :workflow-actions [{:id 1 :type "set-activity" :activity "home-introduce" :order 1}
                      {:id 2 :type "set-activity" :activity "see-saw" :order 2}
                      {:id 3 :type "set-activity" :activity "swings" :order 3}
                      {:id 4 :type "set-activity" :activity "sandbox" :order 4}
                      {:id 5 :type "set-activity" :activity "volleyball" :order 5}
                      {:id 6 :type "set-activity" :activity "hide-n-seek" :order 5}]
   :default-progress {:current-scene "home"
                      :current-activity "home"
                      :variables {:last-location "home"}
                      :current-lesson 1
                      :sets {:concepts "ls1"
                             :assessment-1 "assessment1"}
                      :workflow-action "finish-activity"
                      :finished-workflow-actions {}}})

(defn get-scene
  [course-id scene-id]
  (get-in courses [course-id scene-id]))
