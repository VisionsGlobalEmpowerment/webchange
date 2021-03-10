(ns webchange.student
  (:require
    [webchange.db.core :refer [*db*] :as db]
    [webchange.progress.core :as progress]))

(comment
  ;complete student progress
  (let [course-slug "spanish"
        student-access-code "2222"
        level 1
        lesson 1
        activity 6
        {student-id :id user-id :user-id} (db/find-student-by-code {:school_id 1 :access_code student-access-code})
        {course-id :id} (db/get-course {:slug course-slug})]
    (progress/complete-individual-progress! course-slug student-id {:lesson lesson
                                                                 :level level
                                                                 :activity activity})
    (-> (db/get-progress {:user_id user-id :course_id course-id})
        :data
        :finished)))
