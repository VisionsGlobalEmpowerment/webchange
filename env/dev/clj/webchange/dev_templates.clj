(ns webchange.dev-templates
  (:require [webchange.course.core :as core]
            [webchange.templates.core :as templates]))

(def user-id 1)

(defn create-test-course
  []
  (let [data {:name "test-course"
              :lang "English"}]
    (-> (core/create-course data user-id)
        second)))

(defn update-activity
  [course-slug scene-slug & options]
  (let [{:keys [history objects actions]} options
        scene-data (core/get-scene-latest-version course-slug scene-slug)
        {:keys [created updated]} (get-in scene-data [:metadata :history])

        original-assets (:assets scene-data)
        preserve-objects (-> scene-data
                             :objects
                             (select-keys objects))
        preserve-actions (-> scene-data
                             :actions
                             (select-keys actions))
        activity (as-> (templates/activity-from-template created) a
                       (reduce #(templates/update-activity-from-template %1 {:data %2}) a updated)
                       (update a :objects merge preserve-objects)
                       (update a :actions merge preserve-actions)
                       (update a :assets #(merge original-assets %)))]
    (-> (core/save-scene! course-slug scene-slug activity user-id)
        second)))

(defn get-dialog-actions
  [course-slug scene-slug]
  (->> (core/get-scene-latest-version course-slug scene-slug)
       :actions
       (filter (fn [[_ {:keys [phrase]}]] (some? phrase)))
       (map first)))

(comment
  "define new test course"
  (def test-course-slug (-> (create-test-course) :slug))
  )
