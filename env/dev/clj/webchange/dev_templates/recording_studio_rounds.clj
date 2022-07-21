(ns webchange.dev-templates.recording-studio-rounds
  (:require [webchange.dev-templates :as t]
            [webchange.templates.core :as templates]
            [webchange.course.core :as core]))

(comment
  (def test-course-slug (-> (t/create-test-course) :slug))
  (def test-course-slug "test-course-english-hkgwpieh")
  (def scene-slug "test-activity")

  (core/update-course-activity-template! test-course-slug scene-slug t/user-id)

  (let [data {:activity-name "Recording Studio Rounds"
              :template-id   46

              :demo-image {:src "/raw/img/elements/airplane.png"}
              :image {:src "/raw/img/elements/ant.png"}

              :name          "Recording Studio Rounds"
              :lang          "English"
              :skills        []}
        activity (templates/activity-from-template data)
        metadata (templates/metadata-from-template data)
        [_ {scene-slug :scene-slug}] (core/create-scene! activity metadata test-course-slug scene-slug [] t/user-id)]
    (str "/courses/" test-course-slug "/editor-v2/" scene-slug))

  ;add round
  (let [data {:action "add-round" :data {:image {:src "/raw/img/elements/acorn.png"}}}
        scene-data (core/get-scene-latest-version test-course-slug scene-slug)
        activity (templates/update-activity-from-template scene-data data)]
    (-> (core/save-scene! test-course-slug scene-slug activity t/user-id)
        first))
  )
