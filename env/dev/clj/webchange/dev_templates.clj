(ns webchange.dev-templates
  (:require [webchange.course.core :as core]
            [webchange.templates.core :as templates]
            [webchange.dataset.library :as datasets-library]))

(def user-id 1)

(defn create-test-course
  []
  (let [data {:name "test-course"
              :lang "English"}]
    (-> (core/create-course data user-id)
        second)))

(defn create-test-course-with-dataset
  []
  (let [data {:name "test-course"
              :lang "English"}
        course (core/create-course data user-id)]
    (datasets-library/create-dataset! (-> course second :slug) 1)
    (-> course
        second)))

(defn dialog-names
  [{:keys [actions]}]
  (->> actions
       (filter (fn [[_ action]] (= "dialog" (:editor-type action))))
       (map first)))

(defn update-activity
  [course-slug scene-slug & options]
  (let [{:keys [history objects actions keep-dialogs]} options
        scene-data (core/get-scene-latest-version course-slug scene-slug)
        {:keys [created updated]} (get-in scene-data [:metadata :history])

        actions (if keep-dialogs
                  (concat actions (dialog-names scene-data))
                  actions)

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
                       (update a :assets #(->> (concat original-assets %)
                                               (flatten)
                                               (distinct))))]
    (-> (core/save-scene! course-slug scene-slug activity user-id)
        second)
    ))

(defn get-dialog-actions
  [course-slug scene-slug]
  (->> (core/get-scene-latest-version course-slug scene-slug)
       :actions
       (filter (fn [[_ {:keys [phrase]}]] (some? phrase)))
       (map first)))

(defn update-activity-metadata
  [course-slug scene-slug]
  (let [{course-id :id} (core/get-course-info course-slug)
        scene-data (core/get-scene-latest-version course-slug scene-slug)
        {:keys [created]} (get-in scene-data [:metadata :history])
        metadata (templates/metadata-from-template created)]
    (core/save-dataset-on-create! course-id scene-slug metadata)))

(comment
  "define new test course"
  (def test-course-slug (-> (create-test-course) :slug))
  )
