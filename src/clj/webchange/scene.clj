(ns webchange.scene
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [camel-snake-kebab.core :refer [->snake_case ->kebab-case]]))

(defn str-without-last
  [str number]
  (.substring (java.lang.String. str) 0 (- (count str) number)))

(defn get-dir-files
  [path]
  (->> path io/resource io/file file-seq (filter #(.isFile %)) (mapv (fn [file] {:filename (.getName file)
                                                                                 :path     (str file)}))))

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
      (fn [result {:keys [filename path]}]
        (assoc
          result
          (-> filename (str-without-last 4) (->kebab-case))
          (-> path io/reader java.io.PushbackReader. edn/read)))
      {}
      files)))
