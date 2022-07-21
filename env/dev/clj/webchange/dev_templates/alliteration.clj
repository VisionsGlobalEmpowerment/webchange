(ns webchange.dev-templates.alliteration
  (:require [webchange.dev-templates :as t]
            [webchange.templates.core :as templates]
            [webchange.course.core :as core]))

(comment
  "/courses/test-course-english-zbvgvgzf/editor-v2/test-activity"

  (def test-course-slug (-> (t/create-test-course-with-dataset) :slug))
  (def test-course-slug "test-course-english-zbvgvgzf")
  (def scene-slug "test-activity")

  (t/update-activity test-course-slug scene-slug :keep-dialogs true)
  (core/update-course-activity-template! test-course-slug scene-slug t/user-id)

  (let [data {:activity-name "Alliteration"
              :template-id   31
              :lang          "English"
              :skills        []}
        activity (templates/activity-from-template data)
        metadata (templates/metadata-from-template data)
        [_ {scene-slug :scene-slug}] (core/create-scene! activity metadata test-course-slug scene-slug [] t/user-id)]
    (str "/courses/" test-course-slug "/editor-v2/" scene-slug))

  )
