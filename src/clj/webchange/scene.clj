(ns webchange.scene
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [camel-snake-kebab.core :refer [->snake_case ->kebab-case]]
            [clojure.tools.logging :as log]))

(defn str-without-last
  [str number]
  (.substring (java.lang.String. str) 0 (- (count str) number)))

(defn get-dir-files
  [path]
  (let [dir-content (->> path io/resource io/file)]
    (if-not (nil? dir-content)
      (->> dir-content
           file-seq
           (filter #(.isFile %)))
      [])))

(defn course-path
  [course-name]
  (str "courses/" course-name "/course.edn"))

(defn scene-path
  [course-name scene-name]
  (-> (str "courses/" course-name "/scenes/" scene-name ".edn")
      ->snake_case))

(defn templates-path
  [course-name]
  (-> (str "courses/" course-name "/templates/")
      ->snake_case))

(defn get-course
  [course-name]
  (let [path (course-path course-name)]
    (-> path io/resource io/reader java.io.PushbackReader. edn/read)))

(defn get-scene
  [course-name scene-name]
  (let [path (scene-path course-name scene-name)]
    (-> path io/resource io/reader java.io.PushbackReader. edn/read)))

(defn get-templates
  [course-name]
  (let [path (templates-path course-name)
        files (get-dir-files path)]
    (reduce
      (fn [result file]
        (assoc
          result
          (-> file .getName (str-without-last 4) (->kebab-case))
          (-> file io/reader java.io.PushbackReader. edn/read)))
      {}
      files)))
