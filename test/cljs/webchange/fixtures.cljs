(ns webchange.fixtures
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :refer [>! chan]]
            [re-frame.core :as re-frame]
            [webchange.interpreter.events :as ie]
            [webchange.fixtures.course-table.course :as table-course]
            [webchange.fixtures.course-table.scene-1 :as table-scene-1]
            [webchange.fixtures.course-table.scene-2 :as table-scene-2]
            [webchange.fixtures.course-table.lesson-sets :as table-lesson-sets]))

(def test-courses
  {"table-course" table-course/data
   "test-course"  {:scenes        ["initial-scene" "next-scene" "last-scene"]
                   :levels [{:lessons [{:activities [{:level 0, :lesson 0, :activity "initial-scene" :scene-id 1}]}]}]}})

(def test-scenes
  {"table-course" {"scene-1" table-scene-1/data
                   "scene-2" table-scene-2/data}
   "test-course"  {"initial-scene" {:assets        []
                                    :objects       {:object-with-state
                                                    {:type   "transparent" :x 50 :y 50 :width 100 :height 100
                                                     :states {:default {:x 50} :test-state {:x 100}}}}
                                    :scene-objects []
                                    :actions       {}
                                    :audio         {}
                                    :triggers      {}
                                    :metadata      {}}}})

(def test-lesson-sets
  {"table-course" table-lesson-sets/data})

(defn get-course
  [course-id]
  (let [c (chan)]
    (go (>! c {:body (get test-courses course-id)}))
    c))

(defn get-scene
  [course-id scene-id]
  (let [c (chan)]
    (go (>! c {:body (get-in test-scenes [course-id scene-id])}))
    c))

(defn get-lesson-sets
  [course-id]
  (let [c (chan)]
    (go (>! c {:body (get test-lesson-sets course-id)}))
    c))

(defn get-progress
  [course-id]
  (let [c (chan)]
    (go (>! c {:body {}}))
    c))

(defn init-scene
  []
  (re-frame/dispatch [::ie/start-course "test-course"]))
