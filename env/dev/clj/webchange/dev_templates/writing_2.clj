(ns webchange.dev-templates.writing-2
  (:require
    [webchange.dev-templates :as t]
    [webchange.templates.core :as templates]
    [webchange.course.core :as core]))

(comment
  (def test-course-slug (-> (t/create-test-course) :slug))
  (def scene-slug "test-activity")

  (t/update-activity test-course-slug scene-slug)

  (let [data {:activity-name "Writing"
              :template-id   42
              :name          "Writing"
              :lang          "English"
              :skills        []
              :text          "dog"}
        activity (templates/activity-from-template data)
        metadata (templates/metadata-from-template data)
        [_ {scene-slug :scene-slug}] (core/create-scene! activity metadata test-course-slug scene-slug [] t/user-id)]
    (str "/courses/" test-course-slug "/editor-v2/" scene-slug)))
