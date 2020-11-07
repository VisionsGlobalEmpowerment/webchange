(ns webchange.scene
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [camel-snake-kebab.core :refer [->snake_case ->kebab-case]]
            [clojure.tools.logging :as log]))

(def template-names
  ["click-pointer" "coaching"])

(defn course-path
  [course-slug]
  (str "courses/" course-slug "/course.edn"))

(defn scene-path
  [course-slug scene-name]
  (-> (str "courses/" course-slug "/scenes/" scene-name ".edn")
      ->snake_case))

(defn template-path
  [course-slug template-name]
  (-> (str "courses/" course-slug "/templates/" template-name ".edn")
      ->snake_case))

(defn get-course
  [course-slug]
  (let [path (course-path course-slug)]
    (-> path io/resource io/reader java.io.PushbackReader. edn/read)))

(defn get-scene
  [course-slug scene-name]
  (let [path (scene-path course-slug scene-name)]
    (-> path io/resource io/reader java.io.PushbackReader. edn/read)))

(defn get-templates
  [course-slug]
  (reduce
    (fn [result name]
      (let [template-url (-> (template-path course-slug name) io/resource)]
        (if template-url
          (assoc result name (-> template-url io/reader java.io.PushbackReader. edn/read))
          result)))
    {}
    template-names))
