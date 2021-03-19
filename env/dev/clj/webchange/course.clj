(ns webchange.course
  (:require
    [webchange.db.core :refer [*db*] :as db]
    [webchange.course.core]))

(comment
  ;get course info
  (let [course-slug "spanish"
        {course-id :id} (db/get-course {:slug course-slug})]
    course-id))
