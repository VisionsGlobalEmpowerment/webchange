(ns webchange.dev-templates.flipbook
  (:require [webchange.dev-templates :as t]
            [webchange.templates.core :as templates]
            [webchange.course.core :as core]))

(comment
  (def test-course-slug "book-title-english-pgbrcrig")
  (def scene-slug "book")

  (core/get-scene-latest-version test-course-slug scene-slug)
  (-> (core/get-scene-latest-version test-course-slug scene-slug)
      (get-in [:objects :page-cover-image]))
  (-> (core/get-scene-latest-version test-course-slug scene-slug)
      (get-in [:metadata]))

  (core/update-activity-template! test-course-slug scene-slug t/user-id)

  (core/save-scene! test-course-slug scene-slug stored-data t/user-id)

  (t/update-activity course-slug scene-slug))
