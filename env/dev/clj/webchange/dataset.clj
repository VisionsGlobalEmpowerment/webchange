(ns webchange.dataset
  (:require
   [java-time :as jt]
   [webchange.db.core :as db]
   [webchange.templates.core :as templates]
   [webchange.course.core :as course]))

(defn update-datasets
  [datasets]
  (doall
    (for [{:keys [id scheme]} datasets]
      (db/update-dataset! {:id     id
                           :scheme scheme}))))
(defn update-course
  [course-id course-data]
  (let [created-at (jt/local-date-time)
        owner-id 1]
    (db/save-course! {:course_id course-id
                      :data course-data
                      :created_at created-at
                      :owner_id owner-id})))

(defn update-fields
  [course-slug]
  (let [;course-slug "spanish"
        {course-id :id} (db/get-course {:slug course-slug})
        {course-name :name image :image-src type :type} (db/get-course-by-id {:id course-id})
        course-data (:data (db/get-latest-course-version {:course_id course-id}))
        scene-names (->> course-data
                         :scene-list
                         keys
                         (map name))
        scenes-metadata (->> scene-names
                             (map #(db/get-scene {:course_id course-id :name %}))
                             (map :id)
                             (map #(db/get-latest-scene-version {:scene_id %}))
                             (map :data)
                             (map :metadata)
                             (map :template-id)
                             (map #(get @templates/templates %))
                             (map :metadata)
                             (map #(select-keys % [:fields :lesson-sets])))
        scenes (zipmap scene-names scenes-metadata)
        datasets (db/get-datasets-by-course {:course_id course-id})]
    (->> datasets
         (map (fn [{:keys [id scheme] :as dataset}]
                (let [scheme (reduce (fn [scheme [scene-slug {fields :fields}]]
                                       (update scheme :fields #(course/merge-fields % fields scene-slug)))
                                     scheme
                                     scenes)]
                  {:id id
                   :scheme scheme})))
         update-datasets)
    (->> scenes
         (filter #(-> % second :lesson-sets))
         (reduce (fn [course-data [scene-name {lesson-sets :lesson-sets}]]
                   (assoc-in course-data [:scene-list (keyword scene-name) :lesson-sets] lesson-sets)) course-data)
         (update-course course-id))))

(comment
  (update-fields "spanish")
  )
