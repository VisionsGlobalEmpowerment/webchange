(ns webchange.dev-templates.cinema
  (:require [webchange.dev-templates :as t]
            [webchange.templates.core :as templates]
            [webchange.templates.library.cinema]
            [webchange.course.core :as core]))

(comment
  "/courses/test-course-english-rqzbmjsu/editor-v2/test-activity"

  (def test-course-slug (-> (t/create-test-course-with-dataset) :slug))
  (def scene-slug "test-activity")

  (t/update-activity test-course-slug scene-slug :keep-dialogs true)
  (core/update-course-activity-template! test-course-slug scene-slug t/user-id)

  (def course-slug "english")
  (def scene-slug "alphabet-letter-video")
  (core/update-course-activity-template! course-slug scene-slug t/user-id)

  (let [data {:activity-name "Cinema"
              :template-id   43
              :characters    [{:name     "teacher"
                               :skeleton "senoravaca"}]
              :lang          "English"
              :skills        []}
        activity (templates/activity-from-template data)
        metadata (templates/metadata-from-template data)
        [_ {scene-slug :scene-slug}] (core/create-scene! activity metadata test-course-slug scene-slug [] t/user-id)]
    (str "/courses/" test-course-slug "/editor-v2/" scene-slug))
  )
