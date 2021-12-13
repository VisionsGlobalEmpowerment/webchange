(ns webchange.dev-templates.categorize-colors
  (:require [webchange.dev-templates :as t]
            [webchange.templates.core :as templates]
            [webchange.course.core :as core]))

(defn- copy-activity
  [course-name-source activity-name]
  (let [scene-data (core/get-scene-latest-version course-name-source activity-name)
        course-name-source-new (-> (t/create-test-course) :slug)]
    (core/save-scene! course-name-source-new activity-name scene-data t/user-id)
    [course-name-source-new
     (str "http://localhost:3000/courses/" course-name-source-new "/editor-v2/" activity-name)
     (str "http://localhost:3000/s/" course-name-source-new "/" activity-name)]))

(comment
  "/courses/test-course-english-mbcakkhf/editor-v2/categorize-colors-2"

  (def test-course-slug (-> (t/create-test-course) :slug))
  (def scene-slug "test-activity")

  (copy-activity "english" "categorize-colors-2")

  (def test-course-slug "test-course-english-mbcakkhf")
  (def scene-slug "categorize-colors-2")
  (core/update-activity-template! test-course-slug scene-slug t/user-id)

  (core/get-scene-latest-version test-course-slug scene-slug)


  (let [data {:activity-name "Categorize - colors"
              :template-id   22
              :lang          "English"
              :skills        []}
        activity (templates/activity-from-template data)
        metadata (templates/metadata-from-template data)
        [_ {scene-slug :scene-slug}] (core/create-scene! activity metadata test-course-slug scene-slug [] t/user-id)]
    (str "/courses/" test-course-slug "/editor-v2/" scene-slug))
  )
