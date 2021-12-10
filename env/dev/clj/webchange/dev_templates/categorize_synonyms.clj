(ns webchange.dev-templates.categorize-synonyms
  (:require [webchange.dev-templates :as t]
            [webchange.templates.core :as templates]
            [webchange.course.core :as core]))

(comment
  (def test-course-slug (-> (t/create-test-course) :slug))
  (def scene-slug "categorize-synonyms")

  (def test-course-slug "english")
  (def scene-slug "categorize-synonyms")

  (core/update-activity-template! test-course-slug scene-slug t/user-id)
  (t/update-activity test-course-slug scene-slug :keep-dialogs true)

  (-> (core/get-scene-latest-version test-course-slug scene-slug)
      ;(get-in [:objects])

      ;(keys)
      ;(sort)
      )

  (str "http://localhost:3000/courses/" test-course-slug "/editor-v2/" scene-slug)

  (let [data {:activity-name "Categorize - synonyms"
              :template-id   29
              :lang          "English"
              :skills        []}
        activity (templates/activity-from-template data)
        metadata (templates/metadata-from-template data)
        [_ {scene-slug :scene-slug}] (core/create-scene! activity metadata test-course-slug scene-slug [] t/user-id)]
    (str "/courses/" test-course-slug "/editor-v2/" scene-slug)))
