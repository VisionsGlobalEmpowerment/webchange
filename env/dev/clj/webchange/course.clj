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
        scene-slug "first-words-book"
        latest-version (course/get-scene-latest-version course-slug scene-slug)]
    (-> latest-version
        :actions
        :open-page)))
