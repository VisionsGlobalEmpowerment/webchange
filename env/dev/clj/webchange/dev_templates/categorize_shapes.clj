(ns webchange.dev-templates.categorize-shapes
  (:require [webchange.dev-templates :as t]
            [webchange.templates.core :as templates]
            [webchange.course.core :as core]))

(comment
  (def test-course-slug (-> (t/create-test-course) :slug))
  (def scene-slug "categorize-shapes")

  (def test-course-slug "english")
  (def scene-slug "categorize-shapes")

  (core/update-activity-template! test-course-slug scene-slug t/user-id)

  (-> (core/get-scene-latest-version test-course-slug scene-slug)
      (get-in [:assets]))

  (-> (core/get-scene-latest-version test-course-slug scene-slug)
      (get-in [:objects])
      (keys))

  (-> (core/get-scene-latest-version test-course-slug scene-slug)
      (get-in [:scene-objects]))

  (-> (core/get-scene-latest-version test-course-slug scene-slug)
      (get-in [:metadata :tracks]))

  (let [data {:activity-name "Categorize - shapes"
              :template-id   30
              :lang          "English"
              :skills        []}
        activity (templates/activity-from-template data)
        metadata (templates/metadata-from-template data)
        [_ {scene-slug :scene-slug}] (core/create-scene! activity metadata test-course-slug scene-slug [] t/user-id)]
    (str "/courses/" test-course-slug "/editor-v2/" scene-slug))
  )
