(ns webchange.migrations.concepts-running
  (:require
    [clojure.tools.logging :as log]
    [clojure.string :as str]
    [mount.core :as mount]
    [webchange.course.core :as course]
    [clojure.java.jdbc :as jdbc]
    [java-time :as jt]
    [webchange.db.core :refer [*db*] :as db]
    [webchange.course.core :as course]))

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

(defn- get-group-for
  [level-idx lesson-idx course-data dataset-id]
  (let [lesson-set-name (get-in course-data [:levels level-idx :lessons lesson-idx :lesson-sets :concepts-group])
        lesson-set (db/get-lesson-set-by-name {:dataset_id dataset-id :name lesson-set-name})]
    (->> lesson-set
         :data
         :items
         (map :id)
         (map #(db/get-dataset-item {:id %}))
         (map :data))))

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

(defn create-scene!
  [course-id scene-name]
  (jdbc/execute! *db* ["INSERT INTO scenes (course_id, name) VALUES (?, ?) RETURNING id" course-id scene-name] {:return-keys true}))

(defn save-scene!
  [course-id scene-name scene-data]
  (let [{scene-id :id} (create-scene! course-id scene-name)
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
  [data {:keys [correct-letter incorrect-letter-1 incorrect-letter-2 incorrect-letter-3] :as args}]
  (let [correct-actions [{:type "set-variable"
                          :var-name "currect-concept"
                          :var-value correct-letter}
                         {:type      "set-attribute"
                          :target    "letter-target"
                          :attr-value correct-letter
                          :attr-name "text"}]
        incorrect-actions (->> [incorrect-letter-1
                                incorrect-letter-2
                                incorrect-letter-3]
                               (remove empty?)
                               (repeat 4)
                               (apply concat)
                               (take 8)
                               (map-indexed (fn [idx letter]
                                              {:type "set-variable" :var-name (str "item-" (inc idx)) :var-value letter})))]
    (-> data
        (assoc-in [:actions :init-concept :data] correct-actions)
        (assoc-in [:actions :init-incorrect :data] incorrect-actions)
        (update-in [:metadata :history :created] merge args))))

(defn- concepts->args
  [concept [one two three]]
  {:correct-letter (:letter concept)
   :incorrect-letter-1 (:letter one)
   :incorrect-letter-2 (:letter two)
   :incorrect-letter-3 (:letter three)})

(defn- process-scene
  [course-id scene-id scene-name scene-data dataset-id level-idx lesson-idx activity-idx]
  (let [course-data (-> (db/get-latest-course-version {:course_id course-id})
                        :data)
        concept (get-concept-for level-idx lesson-idx course-data dataset-id)
        group (get-group-for level-idx lesson-idx course-data dataset-id)
        activity-data (-> scene-data
                          (merge-concept-dialogs concept)
                          (set-created-params (concepts->args concept group)))
        new-scene-name (str scene-name "-" (:letter concept))
        course-scene (-> course-data :scene-list
                         (get (keyword scene-name)))
        course-scene-name (-> course-scene
                              (get :name)
                              (generate-name (:letter concept)))]
    (save-scene! course-id new-scene-name activity-data)
    (save-course! course-id (-> course-data
                                (assoc-in [:scene-list (keyword new-scene-name)] {:name course-scene-name
                                                                                  :preview (:preview course-scene)})
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
        scenes ["running-with-letters"]]
    (doseq [course-slug courses
            scene-name scenes]
      (migrate-scene course-slug scene-name))))

