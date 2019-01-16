(ns webchange.course.core
  (:require [webchange.db.core :refer [*db*] :as db]
            ))

(defn get-course-data
  [course-name]
  (let [{course-id :id} (db/get-course {:name course-name})
        latest-version (db/get-course-version {:course_id course-id})]
    (:data latest-version)))

(defn get-scene-data
  [course-name scene-name]
  (let [{course-id :id} (db/get-course {:name course-name})
        {scene-id :id} (db/get-scene {:course_id course-id :name scene-name})
        latest-version (db/get-scene-version {:scene_id scene-id})]
    (:data latest-version)))