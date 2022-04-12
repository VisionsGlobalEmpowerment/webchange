(ns webchange.progress.course-progress
  (:require
    [webchange.progress.utils :refer [idx->keyword]]))

(defn- ->lesson
  [idx lesson]
  [(idx->keyword idx) (->> (:activities lesson)
                           (map :unique-id)
                           (vec))])

(defn- ->level
  [idx level]
  [(idx->keyword idx) (->> (:lessons level)
                           (map-indexed ->lesson)
                           (vec))])

(defn course-data->progress
  [course-data]
  (->> (get course-data :levels [])
       (map-indexed ->level)))
