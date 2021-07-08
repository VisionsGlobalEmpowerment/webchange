(ns webchange.dev-templates.karaoke
  (:require [webchange.dev-templates :as t]
            [webchange.templates.core :as templates]
            [webchange.course.core :as core]))

(comment

  (def test-course-slug (-> (t/create-test-course) :slug))
  (def scene-slug "test-activity")

  (def test-course-slug "english")
  (def scene-slug "rhyme-karaoke-twinkle")

  (core/update-activity-template! test-course-slug scene-slug t/user-id)

  (let [data {:activity-name "Karaoke"
              :template-id   36
              :lang          "English"
              :skills        []
              :video {:src "/upload/MTOFZZSSFMLBXNZI.mp4"}
              :image1-2 {:src "/raw/img/elements/acorn.png"}
              :image1-3 {:src "/raw/img/elements/ant.png"}
              :image2-1 {:src "/raw/img/elements/apple.png"}
              :image2-2 {:src "/raw/img/elements/arm.png"}
              :image2-3 {:src "/raw/img/elements/arrow.png"}
              }
        activity (templates/activity-from-template data)
        metadata (templates/metadata-from-template data)
        [_ {scene-slug :scene-slug}] (core/create-scene! activity metadata test-course-slug scene-slug [] t/user-id)]
    (str "/courses/" test-course-slug "/editor-v2/" scene-slug)))
