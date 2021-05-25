(ns webchange.dev-templates.first-words-book
  (:require [webchange.dev-templates :as t]
            [webchange.templates.core :as templates]
            [webchange.course.core :as core]))

(comment
  (def test-course-slug (-> (t/create-test-course) :slug))
  (def scene-slug "test-activity")

  (def test-course-slug "english")
  (def scene-slug "first-words-book")
  (t/update-activity test-course-slug scene-slug :keep-dialogs true)
  (core/update-activity-template! test-course-slug scene-slug t/user-id)

  (let [data {:activity-name "Book"
              :template-id   44
              :letters       "Aa"
              :subtitle      "The letter a is for..."
              :text1         "apple"
              :image1        {:src "/raw/img/elements/apple.png"}
              :text2         "apple"
              :image2        {:src "/raw/img/elements/apple.png"}
              :text3         "apple"
              :image3        {:src "/raw/img/elements/apple.png"}
              :text4         "apple"
              :image4        {:src "/raw/img/elements/apple.png"}
              :text5         "apple"
              :image5        {:src "/raw/img/elements/apple.png"}
              :text6         "apple"
              :image6        {:src "/raw/img/elements/apple.png"}

              :lang          "English"
              :skills        []}
        activity (templates/activity-from-template data)
        metadata (templates/metadata-from-template data)
        [_ {scene-slug :scene-slug}] (core/create-scene! activity metadata test-course-slug scene-slug [] t/user-id)]
    (str "/courses/" test-course-slug "/editor-v2/" scene-slug))
  )
