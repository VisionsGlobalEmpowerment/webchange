(ns webchange.fixtures
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :refer [>! chan]]
            [re-frame.core :as re-frame]
            [webchange.interpreter.events :as ie]))

(def test-course
  {:initial-scene "initial-scene"
   :preload       ["initial-scene", "next-scene"]
   :scenes ["initial-scene" "next-scene" "last-scene"]})

(def initial-scene
  {:assets []
   :objects {:object-with-state
             {:type "transparent" :x 50 :y 50 :width 100 :height 100
              :states {:default {:x 50} :test-state {:x 100}}}}
   :scene-objects []
   :actions {}
   :audio {}
   :triggers {}
   :metadata {}
   })

(defn get-course
  [course-id]
  (let [c (chan)]
    (go (>! c {:body test-course}))
    c))

(defn get-scene
  [course-id scene-id]
  (let [c (chan)]
    (go (>! c {:body initial-scene}))
    c))

(defn init-scene
  []
  (re-frame/dispatch [::ie/start-course "test-course"]))