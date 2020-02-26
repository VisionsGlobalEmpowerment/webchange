(ns webchange.resources.core-activities
  (:require
    [camel-snake-kebab.core :refer [->Camel_Snake_Case]]
    [clojure.string :as s]
    [webchange.course.core :as course]
    [webchange.resources.utils :refer [find-resources]]
    [webchange.resources.get-dataset-resources :refer [get-dataset-resources]]))

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
  [course-name scene-name]
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
                                               :resources (-> (concat (get-scene-resources course-name scene-name)
                                                                      (get-dataset-resources course-name [scene-name]))
                                                              (distinct))})
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
        initial-scene-resources (get-scene-resources course-name initial-scene)
        datasets-resources (get-dataset-resources course-name [initial-scene])]
    {:resources (-> (concat initial-scene-resources
                            datasets-resources)
                    (distinct))
     :endpoints [(str "/api/courses/" course-name)
                 (str "/api/courses/" course-name "/lesson-sets")
                 (str "/api/courses/" course-name "/scenes/" initial-scene)
                 (str "/api/courses/" course-name "/current-progress")]}))
