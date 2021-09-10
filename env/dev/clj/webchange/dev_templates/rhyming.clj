(ns webchange.dev-templates.rhyming
  (:require [webchange.dev-templates :as t]
            [webchange.templates.core :as templates]
            [webchange.course.core :as core]))

(comment
  (def test-course-slug (-> (t/create-test-course) :slug))
  (def test-course-slug "test-course-english-hxegcfhy")
  (def scene-slug "test-activity")

  (let [data {:template-id 27
              :name        "Rhyming Activity"
              :left        "Left G"
              :right       "Right G"}
        activity (templates/activity-from-template data)
        metadata (templates/metadata-from-template data)
        [_ {scene-slug :scene-slug}] (core/create-scene! activity metadata test-course-slug scene-slug [] t/user-id)]
    (str "/courses/" test-course-slug "/editor-v2/" scene-slug))

  (do (core/update-activity-template! test-course-slug scene-slug t/user-id)
      (t/update-activity test-course-slug scene-slug))

  (-> (core/get-scene-latest-version test-course-slug scene-slug)
      (get-in [:objects :right-gate :scale])))
