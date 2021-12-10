(ns webchange.dev-templates.categorize-colors
  (:require [webchange.dev-templates :as t]
            [webchange.templates.core :as templates]
            [webchange.course.core :as core]))

(comment
  "/courses/test-course-english-rqzbmjsu/editor-v2/test-activity"

  (def test-course-slug (-> (t/create-test-course) :slug))
  (def scene-slug "test-activity")

  (def test-course-slug "english")
  (def scene-slug "categorize-colors-2")

  (core/update-activity-template! test-course-slug scene-slug t/user-id)

  (let [data {:activity-name "Categorize - colors"
              :template-id   22
              :lang          "English"
              :skills        []}
        activity (templates/activity-from-template data)
        metadata (templates/metadata-from-template data)
        [_ {scene-slug :scene-slug}] (core/create-scene! activity metadata test-course-slug scene-slug [] t/user-id)]
    (str "/courses/" test-course-slug "/editor-v2/" scene-slug))
  )
