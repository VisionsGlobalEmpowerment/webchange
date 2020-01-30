(ns webchange.migrations.put-english-course-to-db
  (:require [webchange.course.core :as course]
            [webchange.scene :as scene]
            [mount.core :as mount]
            [clojure.tools.logging :as log]))

(def course-name "english")
(def owner-id 1) ;demo user

(defn migrate-up [config]
  (mount/start)
  (let [course-data (scene/get-course course-name)
        scene-names (->> course-data :scene-list keys (map name))]
    (course/save-course! course-name course-data owner-id)
    (doseq [scene-name scene-names]
      (let [scene-data (scene/get-scene course-name scene-name)]
        (course/save-scene! course-name scene-name scene-data owner-id)))))

(defn migrate-down [config])