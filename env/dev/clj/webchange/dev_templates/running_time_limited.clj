(ns webchange.dev-templates.running-time-limited
  (:require [webchange.dev-templates :as t]
            [webchange.templates.core :as templates]
            [webchange.course.core :as core]))

(comment
  (def course-slug "my-course-eng-kuvdibec")
  (def test-course-slug (-> (t/create-test-course-with-dataset) :slug))
  (def scene-slug "run-with-timer")

  (let [data {:activity-name "Running"
              :template-id   34
              :name          "Running"
              :lang          "English"
              :skills        []
              :time 30}
        activity (templates/activity-from-template data)
        metadata (templates/metadata-from-template data)
        [_ {scene-slug :scene-slug}] (core/create-scene! activity metadata test-course-slug scene-slug [] t/user-id)]
    (str "/courses/" test-course-slug "/editor-v2/" scene-slug))


  (t/update-activity test-course-slug scene-slug :keep-dialogs true)
  (t/update-activity-metadata test-course-slug scene-slug)


  ;; update activity
  (let [dialog-action (t/get-dialog-actions course-slug scene-slug)
        update-result (t/update-activity course-slug scene-slug :actions dialog-action)
        inspect-object :mari]
    (cond
      (some? inspect-object) (get-in update-result [:data :objects inspect-object])
      :else update-result))
  
  (let [course-slug "english"
        scene-slug "running-with-letters-a"]
    (core/get-scene-latest-version course-slug scene-slug))

  (let [user-id 1
        course-slug "english"
        scene-slug "running-with-letters-a"
        template-options {:action "template-options"
                          :data {:time 30,
                                 :speed "4",
                                 :template-id 34,
                                 :correct-letter "a",
                                 :incorrect-letter-1 "o",
                                 :incorrect-letter-2 "s",
                                 :incorrect-letter-3 "m"}}]
    (core/update-activity! course-slug scene-slug template-options user-id))
  )
