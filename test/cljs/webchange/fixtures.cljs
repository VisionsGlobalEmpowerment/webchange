(ns webchange.fixtures
  (:require 
    [webchange.fixtures.course-table.course :as table-course]
    [webchange.fixtures.course-table.scene-1 :as table-scene-1]
    [webchange.fixtures.course-table.scene-2 :as table-scene-2]
    [webchange.fixtures.course-table.lesson-sets :as table-lesson-sets]))

(def test-courses
  {"table-course" table-course/data
   "test-course"  {:scenes        ["initial-scene" "next-scene" "last-scene"]
                   :levels [{:lessons [{:activities [{:level 0, :lesson 0, :activity "initial-scene" :scene-id 1}]}]}]}})

(def test-scenes
  {1 {:assets        []
      :objects       {:object-with-state
                      {:type   "transparent" :x 50 :y 50 :width 100 :height 100
                       :states {:default {:x 50} :test-state {:x 100}}}}
      :scene-objects []
      :actions       {}
      :audio         {}
      :triggers      {}
      :metadata      {}}
   2 table-scene-1/data
   3 table-scene-2/data})

(def test-lesson-sets
  {"table-course" table-lesson-sets/data})

(defn get-course
  [course-id]
  (get test-courses course-id))

(defn get-scene
  [scene-id]
  (get-in test-scenes [scene-id]))

(defn get-lesson-sets
  [course-id]
  (get test-lesson-sets course-id))

(defn get-progress
  [_course-id]
  {})
