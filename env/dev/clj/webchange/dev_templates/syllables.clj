(ns webchange.dev-templates.syllables
  (:require [webchange.dev-templates :as t]
            [webchange.templates.core :as templates]
            [webchange.course.core :as core]))

(comment
  (def test-course-slug (-> (t/create-test-course) :slug))
  (def scene-slug "syllables")

  (def test-course-slug "english")
  (def scene-slug "syllables-1")

  (def test-course-slug "test-course-english-jsbqsmnp")
  (def scene-slug "syllables")

  (core/update-activity-template! test-course-slug scene-slug t/user-id)
  (core/get-scene-latest-version test-course-slug scene-slug)

  (-> (core/save-scene! test-course-slug scene-slug data t/user-id)
        first)

  (t/update-activity course-slug scene-slug)

  (let [data {:activity-name "Syllables"
              :template-id   33
              :characters    [{:name     "teacher"
                               :skeleton "senoravaca"}]
              :lang          "English"
              :skills        []}
        activity (templates/activity-from-template data)
        metadata (templates/metadata-from-template data)
        [_ {scene-slug :scene-slug}] (core/create-scene! activity metadata test-course-slug scene-slug [] t/user-id)]
    (str "/courses/" test-course-slug "/editor-v2/" scene-slug)))

