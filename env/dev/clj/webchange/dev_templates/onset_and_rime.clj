(ns webchange.dev-templates.onset-and-rime
  (:require [webchange.dev-templates :as t]
            [webchange.templates.core :as templates]
            [webchange.course.core :as core]))

(comment
  (def test-course-slug (-> (t/create-test-course) :slug))

  (def test-course-slug "english")
  (def scene-slug "onset--rime--an")

  (core/update-activity-template! test-course-slug scene-slug t/user-id)
  )
