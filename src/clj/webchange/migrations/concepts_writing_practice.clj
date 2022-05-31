(ns webchange.migrations.concepts-writing-practice
  (:require
    [clojure.tools.logging :as log]
    [clojure.string :as str]
    [mount.core :as mount]
    [webchange.course.core :as course]
    [clojure.java.jdbc :as jdbc]
    [java-time :as jt]
    [webchange.db.core :refer [*db*] :as db]))


(def owner-id 1)

(defn- get-concept-for
  [level-idx lesson-idx course-data dataset-id]
  (let [lesson-set-name (get-in course-data [:levels level-idx :lessons lesson-idx :lesson-sets :concepts-single])
        lesson-set (db/get-lesson-set-by-name {:dataset_id dataset-id :name lesson-set-name})
        dataset-item-id     (-> lesson-set
                                :data
                                :items
                                first
                                :id)]
    (-> (db/get-dataset-item {:id dataset-item-id})
        :data)))

(defn- merge-dialog
  [action concept]
  (let [is-concept? (and (= "action" (:type action)) (some? (:from-var action)))
        is-coll? (or (= "sequence-data" (:type action)) (= "parallel" (:type action)))]
    (cond
      is-concept?
      (-> (get concept (-> action :from-var first :var-property keyword)) :data first)

      is-coll?
      (update action :data #(map (fn [inner-action] (merge-dialog inner-action concept)) %))

      :else
      action)))

(defn- merge-concept-dialogs
  [data concept]
  (let [dialogs (->> data
                     :actions
                     (filter (fn [[action-name action]] (= "dialog" (:editor-type action))))
                     (map (fn [[action-name action]] [action-name (merge-dialog action concept)]))
                     (into {}))]
    (update data :actions merge dialogs)))

(defn save-scene!
  [course-id scene-name scene-data]
  (let [[{scene-id :id}] (db/create-scene! {:course_id course-id :name scene-name})
        created-at (jt/local-date-time)]
    (db/save-scene! {:scene_id    scene-id
                     :data        scene-data
                     :owner_id    owner-id
                     :created_at  created-at
                     :description "Migrate"})))

(defn- save-course!
  [course-id data]
  (let [created-at (jt/local-date-time)]
    (db/save-course! {:course_id  course-id
                      :data       data
                      :owner_id   owner-id
                      :created_at created-at})))

(defn- generate-name
  [old-name concept-letter]
  (str old-name " (" concept-letter ")"))

(defn- set-created-params
  [data args]
  (-> data
      (update-in [:metadata :history :created] merge args)))

(defn- concept->args
  [concept]
  {:letter (:letter concept)})

(defn- process-scene
  [course-id scene-id scene-name scene-data dataset-id level-idx lesson-idx activity-idx]
  (let [course-data (-> (db/get-latest-course-version {:course_id course-id})
                        :data)
        concept (get-concept-for level-idx lesson-idx course-data dataset-id)
        activity-data (-> scene-data
                          (merge-concept-dialogs concept)
                          (set-created-params (concept->args concept)))
        new-scene-name (str scene-name "-" (:letter concept))
        course-scene-name (-> course-data :scene-list
                              (get (keyword scene-name))
                              (get :name)
                              (generate-name (:letter concept)))]
    (save-scene! course-id new-scene-name activity-data)
    (save-course! course-id (-> course-data
                                (assoc-in [:scene-list (keyword new-scene-name)] {:name course-scene-name})
                                (assoc-in [:levels level-idx :lessons lesson-idx :activities activity-idx :activity] new-scene-name)))))

(defn- process-course
  [course-id course-data scene-id scene-name scene-data dataset-id]
  (doseq [[level-idx level] (->> course-data :levels (map-indexed vector))
          [lesson-idx lesson] (->> level :lessons (map-indexed vector))
          [activity-idx activity] (->> lesson :activities (map-indexed vector))]
    (when (= scene-name (:activity activity))
      (process-scene course-id scene-id scene-name scene-data dataset-id
                     level-idx lesson-idx activity-idx))))

(defn migrate-scene
  [course-slug scene-name]
  (let [{course-id :id} (db/get-course {:slug course-slug})
        course-data (-> (db/get-latest-course-version {:course_id course-id})
                        :data)
        {scene-id :id} (db/get-scene {:course_id course-id :name scene-name})
        scene-data (-> (db/get-latest-scene-version {:scene_id scene-id})
                       :data)
        dataset-id (-> (db/get-datasets-by-course {:course_id course-id})
                       first
                       :id)]
    (process-course course-id course-data scene-id scene-name scene-data dataset-id)))

(defn migrate-up
  []
  (let [courses ["english" "spanish"]
        scenes ["writing-practice-v-2"]]
    (doseq [course-slug courses
            scene-name scenes]
      (migrate-scene course-slug scene-name))))

