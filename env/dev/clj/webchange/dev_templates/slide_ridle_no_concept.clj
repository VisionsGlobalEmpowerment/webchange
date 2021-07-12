(ns webchange.dev-templates.slide-ridle-no-concept
  (:require [webchange.dev-templates :as t]
            [webchange.templates.core :as templates]
            [webchange.course.core :as core]))

(comment
  (def test-course-slug (-> (t/create-test-course) :slug))
  (def test-course-slug "test-course-english-hkgwpieh")
  (def scene-slug "test-activity")

  (core/update-activity-template! test-course-slug scene-slug t/user-id)

  (let [data {:activity-name "Slide Riddle No Concapt"
              :template-id   38

              :name          "Slide Riddle No Concapt"
              :lang          "English"
              :skills        []}
        activity (templates/activity-from-template data)
        metadata (templates/metadata-from-template data)
        [_ {scene-slug :scene-slug}] (core/create-scene! activity metadata test-course-slug scene-slug [] t/user-id)]
    (str "/courses/" test-course-slug "/editor-v2/" scene-slug))

  ;add round
  (let [data {:action "add-round" :data {:image-correct {:src "/raw/img/elements/acorn.png"}
                                         :image-wrong-1 {:src "/raw/img/elements/airplane.png"}
                                         :image-wrong-2 {:src "/raw/img/elements/ant.png"}}}
        scene-data (core/get-scene-latest-version test-course-slug scene-slug)
        activity (templates/update-activity-from-template scene-data data)]
    (-> (core/save-scene! test-course-slug scene-slug activity t/user-id)
        first))
  )
