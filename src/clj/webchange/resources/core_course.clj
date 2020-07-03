(ns webchange.resources.core-course
  (:require
    [camel-snake-kebab.core :refer [->Camel_Snake_Case]]
    [clojure.string :as s]
    [webchange.course.core :as course]
    [webchange.resources.utils :refer [find-resources]]
    [webchange.resources.get-dataset-resources :refer [get-dataset-resources]]))

(defn- get-scene-resources
  [course-slug scene-name]
  (->> scene-name
       (course/get-scene-data course-slug)
       (find-resources)))

(defn- data->lesson-id
  [{:keys [level]} {:keys [lesson]}]
  (+ (* level 1000) lesson))

(defn- lesson-id->data
  [lesson-id]
  {:level  (quot lesson-id 1000)
   :lesson (mod lesson-id 1000)})

(defn get-course-lessons
  [course-slug]
  (let [course-data (course/get-course-data course-slug)]
    (->> (:levels course-data)
         (map (fn [level]
                (let [level-data {:level-name (:name level)}]
                  (map (fn [lesson]
                         (merge level-data
                                {:id          (data->lesson-id level lesson)
                                 :lesson-name (:name lesson)}))
                       (:lessons level)))))
         (reduce concat))))

(defn get-lesson-resources
  [course-slug level-number lesson-number]
  (let [course-data (course/get-course-data course-slug)
        level (some (fn [level] (and (= (:level level) level-number) level)) (:levels course-data))
        lesson (some (fn [lesson] (and (= (:lesson lesson) lesson-number) lesson)) (:lessons level))]
    (let [transit-scenes (->> (:scene-list course-data)
                              (filter (fn [[_ scene-data]] (< 1 (->> scene-data :outs count))))
                              (map (fn [[scene-name _]] (name scene-name))))
          activities (->> (:activities lesson)
                          (map :activity)
                          (concat transit-scenes))
          activities-resources (reduce (fn [result activity-name]
                                         (concat result (get-scene-resources course-slug activity-name)))
                                       []
                                       activities)
          lesson-sets (->> (:lesson-sets lesson) (vals))
          lesson-sets-resources (reduce (fn [result lesson-set-name]
                                          (concat result (get-dataset-resources course-slug lesson-set-name activities)))
                                        []
                                        lesson-sets)
          overall-resources (->> (concat activities-resources
                                         lesson-sets-resources)
                                 (flatten)
                                 (distinct))
          endpoints (->> activities
                         (map (fn [activity-name]
                                (str "/api/courses/" course-slug "/scenes/" activity-name)))
                         (concat ["/api/schools/current"
                                  (str "/api/courses/" course-slug)
                                  (str "/api/courses/" course-slug "/lesson-sets")]))]
      {:resources overall-resources
       :endpoints endpoints})))

(defn get-lessons-resources
  [course-slug lesson-ids]
  (->> lesson-ids
       (map (fn [id-str] (Integer/parseInt id-str)))
       (map lesson-id->data)
       (map (fn [{:keys [level lesson]}]
              (get-lesson-resources course-slug level lesson)))
       (reduce (fn [result {:keys [resources endpoints]}]
                 (-> result
                     (update :resources concat resources)
                     (update :resources distinct)
                     (update :endpoints concat endpoints)
                     (update :endpoints distinct)))
               {:resources []
                :endpoints []})))
