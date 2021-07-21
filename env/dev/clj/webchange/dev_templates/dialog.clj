(ns webchange.dev-templates.dialog
  (:require [webchange.dev-templates :as t]
            [webchange.templates.core :as templates]
            [webchange.course.core :as core]))

(comment
  (def test-course-slug (-> (t/create-test-course) :slug))
  (def test-course-slug "test-course-english-zbvgvgzf")
  (def scene-slug "test-activity")

  (core/update-activity-template! test-course-slug scene-slug t/user-id)

  (let [data {:activity-name "Dialog"
              :template-id   4
              :lang          "English"
              :skills        []

              :characters    [{:name     "child"
                               :skeleton "child"}]}
        activity (templates/activity-from-template data)
        metadata (templates/metadata-from-template data)
        [_ {scene-slug :scene-slug}] (core/create-scene! activity metadata test-course-slug scene-slug [] t/user-id)]
    (str "/courses/" test-course-slug "/editor-v2/" scene-slug))

  )
