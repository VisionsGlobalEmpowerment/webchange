(ns webchange.course
  (:require
    [webchange.db.core :refer [*db*] :as db]
    [webchange.course.core :as course]))

(comment
  ;get course info
  (let [course-slug "spanish"
        {course-id :id} (db/get-course {:slug course-slug})]
    course-id)

  (let [course-slug "english"
        data (course/get-course-latest-version course-slug)]
    (-> data
        :levels))

  (let [course-slug "test-english-xuomidin"
        data (course/get-course-data course-slug)]
    (-> data
        ))

  (let [course-slug "english"
        scene-slug "first-words-book"
        latest-version (course/get-scene-latest-version course-slug scene-slug)]
    (-> latest-version
        :actions
        :open-page))

  (let [activity-id 691]
    (course/update-activity-template! activity-id 1))
  (let [activity-id 1786]
    (course/update-activity-template! activity-id 1))
  
  (let [activity-id 1786]
    (course/get-activity-current-version activity-id))
  (let [activity-id 1421]
    (course/get-activity-current-version activity-id)))
