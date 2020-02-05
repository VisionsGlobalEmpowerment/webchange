(ns webchange.resources.core-activities
  (:require
    [camel-snake-kebab.core :refer [->Camel_Snake_Case]]
    [clojure.string :as s]
    [webchange.course.core :as course]
    [webchange.dataset.core :as dataset]
    [webchange.resources.utils :refer [find-resources]]))

(defn- get-course-levels
  [course-data]
  (->> (:workflow-actions course-data)
       (filter :level)
       (group-by :level)
       (reduce (fn [result [level-number level-activities]]
                 (conj result {:id         (->> level-number (str "level-") (keyword))
                               :name       (->> level-number (str "Level "))
                               :activities (->> level-activities
                                                (filter (fn [activity]
                                                          (= "set-activity" (:type activity))))
                                                (map :activity)
                                                (concat ["map"])
                                                (distinct))}))
               [])))

(defn- get-scene-resources
  [scene-name course-name]
  (->> scene-name
       (course/get-scene-data course-name)
       (find-resources)))

(defn- add-scenes-data
  [levels course-name]
  (->> levels
       (map (fn [{:keys [activities] :as level}]
              (assoc level :activities (map (fn [scene-name]
                                              {:id        (keyword scene-name)
                                               :name      (-> scene-name
                                                              (->Camel_Snake_Case)
                                                              (s/replace "_" " "))
                                               :endpoint  (str "/api/courses/" course-name "/scenes/" scene-name)
                                               :resources (get-scene-resources scene-name course-name)})
                                            activities))))))

(defn get-activities-resources
  [course-name]
  (-> course-name
      (course/get-course-data)
      (get-course-levels)
      (add-scenes-data course-name)))

(defn get-start-resources
  [course-name]
  (let [initial-scene (:initial-scene (course/get-course-data course-name))
        initial-scene-resources (->> course-name
                                     (get-scene-resources initial-scene))
        lessons-resources (->> course-name
                               (dataset/get-course-lessons)
                               (find-resources))]
    {:resources (-> (concat initial-scene-resources lessons-resources)
                    (distinct))
     :endpoints [(str "/api/courses/" course-name)
                 (str "/api/courses/" course-name "/lesson-sets")
                 (str "/api/courses/" course-name "/scenes/" initial-scene)
                 (str "/api/courses/" course-name "/current-progress")]}))
