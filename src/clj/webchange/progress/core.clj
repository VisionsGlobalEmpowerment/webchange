(ns webchange.progress.core
  (:require [webchange.db.core :refer [*db*] :as db]
            [clojure.tools.logging :as log]
            [camel-snake-kebab.extras :refer [transform-keys]]
            [camel-snake-kebab.core :refer [->snake_case_keyword]]
            [webchange.auth.core :as auth]
            [webchange.events :as events]
            [webchange.class.core :as class]
            [webchange.course.core :as course]
            [java-time :as jt]))

(defn get-current-progress [course-name student-id]
  (let [{course-id :id} (db/get-course {:name course-name})
        progress (db/get-progress {:user_id student-id :course_id course-id})]
    [true {:progress (:data progress)}]))

(defn get-class-profile [course-name class-id]
  (let [{course-id :id} (db/get-course {:name course-name})
        stats (->> (db/get-course-stats {:class_id class-id :course_id course-id})
                   (map class/with-user)
                   (map class/with-student-by-user))]
    [true {:stats stats
           :class-id class-id
           :course-name course-name}]))

(defn level->grid
  [actions f]
  (->> actions
       (group-by :lesson)
       (#(into (sorted-map) %))
       (map (fn [[l a]] {:name (str "L" l)
                         :values (map f a)}))))

(defn with-stat
  [{id :id :as action} stats-map]
  (assoc action :stat (get stats-map id)))

(defn workflow->grid
  [actions stats-map f]
  (->> actions
       (remove #(not= "set-activity" (:type %)))
       (map #(with-stat % stats-map))
       (group-by :level)
       (map (fn [[l a]] [l (level->grid a f)]))
       (#(into (sorted-map) %))))

(defn ->percentage [value] (-> value (* 100) float Math/round))

(defn score->value [score is-scored]
  (let [correct (-> (:correct score) (or 1))
        incorrect (-> (:incorrect score) (or 0))]
    (cond
      (and score is-scored) (-> correct
                                (- incorrect)
                                (/ correct)
                                ->percentage)
      (and score) 100
      :else nil)))

(defn activity->score
  [activity]
  {:id (:id activity)
   :label (:activity activity)
   :started (-> activity :stat :data boolean)
   :finished (-> activity :stat :data :score boolean)
   :percentage (score->value (-> activity :stat :data :score) (-> activity :scored))
   :value (score->value (-> activity :stat :data :score) (-> activity :scored))})

(defn time->percentage
  [time expected]
  (if (and time expected)
    (let [elapsed (-> time (/ 1000) float Math/round)]
      (if (> elapsed expected)
        (-> (/ expected elapsed) ->percentage)
        100))))

(defn time->value
  [time]
  (let [elapsed (-> time (/ 1000) float Math/round)
        minutes (int (/ elapsed 60))
        seconds (int (- elapsed (* minutes 60)))]
    (str minutes "m " seconds "s")))

(defn activity->time
  [activity]
  {:id (:id activity)
   :label (:activity activity)
   :started (-> activity :stat :data boolean)
   :finished (-> activity :stat :data :score boolean)
   :percentage (time->percentage (-> activity :stat :data :time-spent) (-> activity :time-expected))
   :value (time->value (-> activity :stat :data (:time-spent 0)))})

(defn get-individual-progress [course-name student-id]
  (let [{user-id :user-id} (db/get-student {:id student-id})
        {course-id :id} (db/get-course {:name course-name})
        course-data (course/get-course-data course-name)
        stats (->> (db/get-user-activity-stats {:user_id user-id :course_id course-id})
                   (map (fn [stat] [(:activity-id stat) stat]))
                   (into {}))]
    [true {:stats stats
           :scores (workflow->grid (:workflow-actions course-data) stats activity->score)
           :times (workflow->grid (:workflow-actions course-data) stats activity->time)}]))

(defn save-events! [owner-id course-id events]
  (doseq [{created-at-string :created-at type :type :as data} events]
    (let [created-at (jt/offset-date-time created-at-string)]
      (db/create-event! {:user_id owner-id :course_id course-id :created_at created-at :type type :data data})
      (events/dispatch (-> data
                           (assoc :user-id owner-id)
                           (assoc :course-id course-id))))))

(defn create-progress! [owner-id course-id data]
  (let [[{id :id}] (db/create-progress! {:user_id owner-id :course_id course-id :data data})]
    [true {:id id}]))

(defn update-progress! [id data]
  (db/save-progress! {:id id :data data})
  [true {:id id}])

(defn save-progress!
  [owner-id course-name {:keys [progress events]}]
  (let [{course-id :id} (db/get-course {:name course-name})]
    (save-events! owner-id course-id events)
    (if-let [{id :id} (db/get-progress {:user_id owner-id :course_id course-id})]
      (update-progress! id progress)
      (create-progress! owner-id course-id progress))))
