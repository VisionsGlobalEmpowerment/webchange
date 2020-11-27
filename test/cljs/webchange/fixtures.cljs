(ns webchange.fixtures
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :refer [>! chan]]
            [re-frame.core :as re-frame]
            [webchange.interpreter.events :as ie]
            [webchange.fixtures.course-table.course :as table-course]
            [webchange.fixtures.course-table.scene-1 :as table-scene-1]
            [webchange.fixtures.course-table.scene-2 :as table-scene-2]))

(def test-courses
  {"table-course" table-course/data
   "test-course"  {:initial-scene "initial-scene"
                   :preload       ["initial-scene" "next-scene"]
                   :scenes        ["initial-scene" "next-scene" "last-scene"]}})

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

(defn init-scene
  []
  (re-frame/dispatch [::ie/start-course "test-course"]))
