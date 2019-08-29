(ns webchange.scene
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [camel-snake-kebab.core :refer [->snake_case]]
            [clojure.tools.logging :as log]))

(defn course-path
  [course-name]
  (str "courses/" course-name "/course.edn"))

(defn scene-path
  [course-name scene-name]
  (-> (str "courses/" course-name "/scenes/" scene-name ".edn")
      ->snake_case))

(defn get-course
  [course-name]
  (let [path (course-path course-name)]
    (-> path io/resource io/reader java.io.PushbackReader. edn/read)))

(defn get-scene
  [course-name scene-name]
  (let [path (scene-path course-name scene-name)]
    (-> path io/resource io/reader java.io.PushbackReader. edn/read)))
