(ns webchange.migrations.add-unique-ids
  (:require [mount.core :as mount]
            [webchange.course.core :as course]
            [webchange.progress.core :as progress]
            [webchange.db.core :refer [*db*] :as db]))

(defn add-unique-ids [levels unique-id]
  (mapv (fn [level]
          (update-in level [:lessons]
            (fn [l]
              (mapv (fn [lesson]
                      (update-in lesson [:activities]
                        (fn [a]
                          (mapv (fn [activity]
                                  (assoc activity :unique-id
                                    (swap! unique-id inc)))
                            a)))) l)))) levels))

(defn map-map [func m]
  (apply merge (map func m)))

(defn k->n [k]
  (Integer/parseInt (name k)))

(defn finished-index->unique-id [finished levels]
  (map-map (fn [[level-key level-value]]
             {level-key
              (map-map (fn [[lesson-key lesson-value]]
                         {lesson-key
                          (mapv (fn [index]
                                  (get-in levels [(k->n level-key) :lessons
                                                  (k->n lesson-key) :activities
                                                  index :unique-id]))
                            lesson-value)})
                level-value)})
    finished))

(defn dissoc-in [map keys]
  (if (= (count keys) 1)
    (dissoc map (nth keys 0))
    (update-in map (butlast keys) dissoc (last keys))))

(defn update-progress [progress course-data]
  (-> progress
    (dissoc-in [:data :next])
    (update-in [:data :finished] #(finished-index->unique-id % (:levels course-data)))))

(defn migrate-up [config]
  (mount/start)
  (let [unique-id (atom -1)
        course-slug "english"
        course-id (:id (db/get-course {:slug course-slug}))
        new-course (-> (db/get-latest-course-version {:course_id course-id})
                     (update-in [:data :levels] #(add-unique-ids % unique-id))
                     (assoc-in [:data :unique-id] @unique-id))

        new-progresses (->> (db/get-progress-for-course {:course_id course-id})
                         (map #(update-progress % (:data new-course))))]
    (db/save-course-data! new-course)
    (doseq [{:keys [id data]} new-progresses]
      (progress/update-progress! id data))))

(defn migrate-down [config]
  (mount/start)
  (print "migrate down"))
