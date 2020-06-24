(ns webchange.resources.core-activities
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

(defn get-activities-resources
  "Returns a list of lessons data.
   Params:
    course-slug <string> - course name
   Return example:
   [{
    :level-number  1
    :level-name    'Level 1'
    :lesson-number 1
    :lesson-name   'Lesson 1'
    :resources     ['/raw/img/casa/door.png', ...]
    :endpoints     ['/api/courses/spanish/scenes/map', ...]
   },
   ...]"
  [course-slug]
  (let [course-data (course/get-course-data course-slug)
        transit-scenes (->> (:scene-list course-data)
                            (filter (fn [[_ scene-data]] (< 1 (->> scene-data :outs count))))
                            (map (fn [[scene-name _]] (name scene-name))))]
    (->> (:levels course-data)
         (map (fn [level]
                (let [level-data {:level-number (:level level)
                                  :level-name   (:name level)}]
                  (map (fn [lesson]
                         (let [activities (->> (:activities lesson)
                                               (map :activity)
                                               (concat transit-scenes))
                               activities-resources (reduce (fn [result activity-name]
                                                              (assoc result (keyword activity-name) (get-scene-resources course-slug activity-name)))
                                                            {}
                                                            activities)
                               lesson-sets (->> (:lesson-sets lesson) (vals))
                               lesson-sets-resources (reduce (fn [result lesson-set-name]
                                                               (assoc result (keyword lesson-set-name) (get-dataset-resources course-slug activities)))
                                                             {}
                                                             lesson-sets)
                               overall-resources (->> (concat (vals activities-resources)
                                                              (vals lesson-sets-resources))
                                                      (flatten)
                                                      (distinct))
                               endpoints (map (fn [activity-name]
                                                (str "/api/courses/" course-slug "/scenes/" activity-name))
                                              activities)]
                           (merge level-data
                                  {:lesson-number (:lesson lesson)
                                   :lesson-name   (:name lesson)
                                   :resources     overall-resources
                                   :endpoints     endpoints})))
                       (:lessons level)))))
         (flatten))))

(defn get-start-resources
  [course-slug]
  (let [initial-scene (:initial-scene (course/get-course-data course-slug))
        initial-scene-resources (get-scene-resources course-slug initial-scene)
        datasets-resources (get-dataset-resources course-slug [initial-scene])]
    {:resources (-> (concat initial-scene-resources
                            datasets-resources)
                    (distinct))
     :endpoints [(str "/api/courses/" course-slug)
                 (str "/api/courses/" course-slug "/lesson-sets")
                 (str "/api/courses/" course-slug "/scenes/" initial-scene)
                 (str "/api/courses/" course-slug "/current-progress")]}))
