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
  (let [data {:action "add-question"
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

(comment
  (defn- copy-activity
    [course-name-source activity-name]
    (let [scene-data (core/get-scene-latest-version course-name-source activity-name)
          course-name-source-new (-> (t/create-test-course) :slug)]
      (core/save-scene! course-name-source-new activity-name scene-data t/user-id)
      [course-name-source-new
       (str "http://localhost:3000/courses/" course-name-source-new "/editor-v2/" activity-name)
       (str "http://localhost:3000/s/" course-name-source-new "/" activity-name)]))

  ;; Book
  (def book-course-slug "amazing-daisy-english-apwoapsx")
  (def book-scene-slug "book")
  (core/get-scene-latest-version book-course-slug book-scene-slug)
  ; copy:
  (copy-activity book-course-slug book-scene-slug)
  (def book-course-slug-copied "test-course-english-fwacvyda")


  ;; IRA

  (def ira-course-slug "english")
  (def ira-scene-slug "interactive-read-aloud-amazing-daisy")
  (core/get-scene-latest-version ira-course-slug ira-scene-slug)
  ; copy:
  (copy-activity ira-course-slug ira-scene-slug)
  (def ira-course-slug-copied "test-course-english-syggwhmo")

  ;; Fix book-link

  (core/save-scene! ira-course-slug-copied
                    ira-scene-slug
                    (-> (core/get-scene-latest-version ira-course-slug-copied ira-scene-slug)
                        (assoc-in [:metadata :saved-props :wizard :book] book-course-slug-copied)
                        (assoc-in [:metadata :history :created :book] book-course-slug-copied))
                    t/user-id)

  (-> (core/get-scene-latest-version ira-course-slug-copied ira-scene-slug)
      (get-in [:objects :book]))
;; sleepy mr sloth
  (-> (core/get-activity-current-version 825))
  ;; ira 1: sleepy mr sloth
  (-> (core/get-activity-current-version 834))
  
  (let [user-id 1
        course-slug "english"
        scene-slug "interactive-read-aloud-newest"
        template-options {:action "template-options"
                          :data {:book-id 825}}]
    (core/update-activity! course-slug scene-slug template-options user-id))
  )
