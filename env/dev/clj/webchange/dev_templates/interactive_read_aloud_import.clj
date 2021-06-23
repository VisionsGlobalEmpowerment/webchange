(ns webchange.dev-templates.interactive-read-aloud-import
  (:require [webchange.dev-templates :as t]
            [webchange.templates.core :as templates]
            [webchange.course.core :as core]))

(comment
  (def test-course-slug (-> (t/create-test-course) :slug))
  (def scene-slug "test-activity")

  (core/update-activity-template! test-course-slug scene-slug t/user-id)

  (let [data {:activity-name "IRA"
              :template-id   45

              :characters    [{:name     "teacher"
                               :skeleton "senoravaca"}]
              :book          "sleepy-mr-sloth-english-hjcnzmqz"

              :name          "Interactive Read Aloud"
              :lang          "English"
              :skills        []}
        activity (templates/activity-from-template data)
        metadata (templates/metadata-from-template data)
        [_ {scene-slug :scene-slug}] (core/create-scene! activity metadata test-course-slug scene-slug [] t/user-id)]
    (str "/courses/" test-course-slug "/editor-v2/" scene-slug))

  ;add dialog
  (let [data {:action "add-dialog" :data {:dialog "dialog one"}}
        scene-data (core/get-scene-latest-version test-course-slug scene-slug)
        activity (templates/update-activity-from-template scene-data data)]
    (-> (core/save-scene! test-course-slug scene-slug activity t/user-id)
        first))

  ;add question
  (let [data {:action        "add-question"
              :data
              {:question-page {:answers  [{:text "answer one"}
                                          {:text "answer two"}
                                          {:text "answer three" :checked true}
                                          {:text "answer four"}]
                               :question "Question one"
                               :img      "/upload/DQHMQLASWJIKMETH.png"}}}
        scene-data (core/get-scene-latest-version test-course-slug scene-slug)
        activity (templates/update-activity-from-template scene-data data)]
    (-> (core/save-scene! test-course-slug scene-slug activity t/user-id)
        first)))
