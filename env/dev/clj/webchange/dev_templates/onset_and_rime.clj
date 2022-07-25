(ns webchange.dev-templates.onset-and-rime
  (:require [webchange.dev-templates :as t]
            [webchange.templates.core :as templates]
            [webchange.course.core :as core]))

(comment
  (def test-course-slug (-> (t/create-test-course) :slug))

  (def test-course-slug "english")
  (def scene-slug "onset--rime--an")

  (core/update-course-activity-template! test-course-slug scene-slug t/user-id)

  (let [course-slug "english"
        scene-slug "onset--rime--an"]
    (core/get-scene-latest-version course-slug scene-slug))

  (let [user-id 1
        course-slug "english"
        scene-slug "onset--rime--an"
        template-options {:action "template-options"
                          :data {:image {:src "/upload/LRHPKTUWNRXSRXDT.png"},
                                 :rounds
                                 [{:id 0,
                                   :image {:src "/upload/KDLVHALMHKFIHCSD.png"},
                                   :left-text "p",
                                   :right-text "an",
                                   :whole-text "pan",
                                   :action-name "add-ball"}
                                  {:id 1,
                                   :image {:src "/upload/VPINYKLXCXYOHBGM.png"},
                                   :left-text "m",
                                   :right-text "an",
                                   :whole-text "man",
                                   :action-name "add-ball"}
                                  {:id 2,
                                   :image {:src "/upload/ZREOWSMQPQWBUKHG.png"},
                                   :left-text "c",
                                   :right-text "an",
                                   :whole-text "can",
                                   :action-name "add-ball"}],
                                 :left-text "c",
                                 :right-text "at",
                                 :whole-text "cat"}}]
    (core/update-activity! course-slug scene-slug template-options user-id))
  )
