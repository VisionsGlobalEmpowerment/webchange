(ns webchange.dev-templates.flipbook
  (:require [webchange.dev-templates :as t]
            [webchange.templates.core :as templates]
            [webchange.course.core :as core]))

(comment
  (def test-course-slug "sleepy-mr-sloth-english-gouuirga")
  (def scene-slug "book")

  (core/get-scene-latest-version test-course-slug scene-slug)
  (-> (core/get-scene-latest-version test-course-slug scene-slug)
      (get-in [:objects :page-cover-image]))
  (-> (core/get-scene-latest-version test-course-slug scene-slug)
      (get-in [:metadata]))

  (core/update-course-activity-template! test-course-slug scene-slug t/user-id)

  (core/save-scene! test-course-slug scene-slug stored-data t/user-id)

  (t/update-activity course-slug scene-slug))

(comment
  (defn- copy-activity
    ([course-name-source activity-name]
     (copy-activity course-name-source activity-name (-> (t/create-test-course) :slug)))
    ([course-name-source activity-name course-name-source-new]
     (let [scene-data (core/get-scene-latest-version course-name-source activity-name)]
       (core/save-scene! course-name-source-new activity-name scene-data t/user-id)
       [(str "http://localhost:3000/courses/" course-name-source-new "/editor-v2/" activity-name)
        (str "http://localhost:3000/s/" course-name-source-new "/" activity-name)])))

  (def course-slug-origin "sizwes-smile-english-hpiimczo")
  (def scene-slug-origin "book")

  (copy-activity course-slug-origin scene-slug-origin)

  (def course-slug-copied "test-course-english-qitwmpwo")
  (copy-activity course-slug-origin scene-slug-origin course-slug-copied)

  (-> (core/get-scene-latest-version course-slug-copied scene-slug-origin)
      (get-in [:objects])
      (select-keys [:page-image-6 :page-47-image-2]))

  (let [scene-id 2006]
    (core/update-activity-template! scene-id t/user-id))

  (let [scene-id 2006]
    (-> (core/get-activity-current-version 2006)
        :objects
        ))
  )
