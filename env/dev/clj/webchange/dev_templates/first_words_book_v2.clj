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
      (get-in [:actions :set-total-spreads-number :var-value])
      ;(keys)
      )

  (update-scene-object course-slug scene-slug
                       (fn [activity-data]
                         (-> activity-data
                             (update-in [:objects :spread-2-left-page] dissoc :visible)
                             (update-in [:objects :spread-2-right-page] dissoc :visible)
                             (update-in [:objects :spread-3-left-page] dissoc :visible)
                             (update-in [:objects :spread-3-right-page] dissoc :visible))))

  (let [course-slug "english"
        scene-slug "first-words-book-letter-a"]
    (core/get-scene-latest-version course-slug scene-slug))

  (let [user-id 1
        course-slug "english"
        scene-slug "first-words-book-letter-a"
        template-options {:action "template-options"
                          :data {:title "Letter",
                                 :letters "Aa",
                                 :spreads
                                 [{:text-left "apple",
                                   :image-left {:src "/upload/SKFEIACYRWGYILJE.png"},
                                   :text-right "alligator",
                                   :action-name "add",
                                   :image-right {:src "/upload/WLYBCHFKLQRFJJUB.png"}}
                                  {:text-left "astronaut",
                                   :image-left {:src "/upload/ITYCSSNXJNBKOYLQ.png"},
                                   :text-right "arrow",
                                   :action-name "add",
                                   :image-right {:src "/upload/PILXQYLMBHGLWNMZ.png"}}
                                  {:text-left "ant",
                                   :image-left {:src "/upload/HUSMVTTZEUZQEBTS.png"},
                                   :text-right "axe",
                                   :action-name "add",
                                   :image-right {:src "/upload/CBXWJZPUTPSAFQZZ.png"}}],
                                 :subtitle "The letter a is for...",
                                 :template-id 49}}]
    (core/update-activity! course-slug scene-slug template-options user-id))
  )
