(ns webchange.dev-templates.flipbook
  (:require [webchange.dev-templates :as t]
            [webchange.templates.core :as templates]
            [webchange.course.core :as core]))

(comment
  (def test-course-slug "sleepy-mr-sloth-english-hjcnzmqz")
  (def scene-slug "book")

  (core/update-activity-template! test-course-slug scene-slug t/user-id)

  (t/update-activity course-slug scene-slug))
