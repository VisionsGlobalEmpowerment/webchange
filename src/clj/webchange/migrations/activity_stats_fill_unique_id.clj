(ns webchange.migrations.activity-stats-fill-unique-id
  (:require
    [clojure.tools.logging :as log]
    [clojure.string :as str]
    [mount.core :as mount]
    [webchange.course.core :as course]
    [clojure.java.jdbc :as jdbc]
    [webchange.db.core :refer [*db*]]))

(defn- get-activity-stats
  []
  (let [stats (jdbc/query *db* ["SELECT * FROM activity_stats;"])]
    stats))

(defn- set-activity-stat-unique-id
  [stat-id unique-id]
  (jdbc/execute! *db* ["UPDATE activity_stats SET unique_id = ? WHERE id = ?" unique-id stat-id]))

(defn- ->unique-id
  [activities activity-name]
  (->> activities
       (filter #(= activity-name (:activity %)))
       (first)
       :unique-id))

(defn- fill-unique-id
  [stat courses]
  (let [course (get courses (:course_id stat))
        [level lesson activity & activity-name-parts] (str/split (:activity_id stat) #"-")
        activity-name (str/join "-" activity-name-parts)
        level (if (empty? level) "0" level)
        lesson (if (empty? lesson) "0" lesson)
        unique-id (->unique-id (get-in course [:levels (Integer/parseInt level)
                                               :lessons (Integer/parseInt lesson)
                                               :activities]) activity-name)]
    (set-activity-stat-unique-id (:id stat) unique-id)))

(defn migrate-up
  [_]
  (mount/start)
  (let [courses {2 (course/get-course-latest-version "spanish")
                 4 (course/get-course-latest-version "english")}]
    (doseq [activity-stat (get-activity-stats)]
      (fill-unique-id activity-stat courses))))

(defn migrate-down
  [_]
  (mount/start))
