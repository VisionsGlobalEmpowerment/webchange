(ns webchange.dev-templates.i-spy
  (:require [webchange.dev-templates :as t]
            [webchange.templates.core :as templates]
            [webchange.course.core :as core]))

(comment
  (def test-course-slug "english")
  (def scene-slug "i-spy-1")

  (core/update-activity-template! test-course-slug scene-slug t/user-id)

  (t/update-activity course-slug scene-slug))
