(ns webchange.dev-templates.first-words-book-v2
  (:require [webchange.dev-templates :as t]
            [webchange.dev-templates.utils :refer [update-scene-object]]
            [webchange.templates.core :as templates]
            [webchange.course.core :as core]))

(comment
  (def course-slug "english")
  (def scene-slug "qqq-2")
  (core/get-scene-latest-version course-slug scene-slug)
  (core/update-activity-template! course-slug scene-slug t/user-id)
  (core/save-scene! course-slug scene-slug {} t/user-id)

  (let [data {:title       "Letter"
              :letters     "Aa"
              :subtitle    "The letter A is for"
              :template-id 49}
        activity (templates/activity-from-template data)
        metadata (templates/metadata-from-template data)
        [_ {scene-slug :scene-slug}] (core/create-scene! activity metadata course-slug scene-slug [] t/user-id)]
    (str "/courses/" course-slug "/editor/" scene-slug))

  (-> (core/get-scene-latest-version course-slug scene-slug)
      (get-in [:objects :spread-3-right-page])
      ;(keys)
      )

  (update-scene-object course-slug scene-slug
                       (fn [activity-data]
                         (-> activity-data
                             (update-in  [:objects :spread-2-left-page] dissoc :visible)
                             (update-in  [:objects :spread-2-right-page] dissoc :visible)
                             (update-in  [:objects :spread-3-left-page] dissoc :visible)
                             (update-in  [:objects :spread-3-right-page] dissoc :visible))))
  )
