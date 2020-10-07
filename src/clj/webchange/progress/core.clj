(ns webchange.progress.core
  (:require [webchange.db.core :refer [*db*] :as db]
            [clojure.tools.logging :as log]
            [camel-snake-kebab.extras :refer [transform-keys]]
            [camel-snake-kebab.core :refer [->snake_case_keyword]]
            [webchange.auth.core :as auth]
            [webchange.progress.activity :as activity]
            [webchange.events :as events]
            [webchange.class.core :as class]
            [webchange.course.core :as course]
            [java-time :as jt]))

(defn get-current-progress [course-slug student-id]
  (let [{course-id :id} (db/get-course {:slug course-slug})
        progress (db/get-progress {:user_id student-id :course_id course-id})]
    [true {:progress (:data progress)}]))

(defn get-class-profile [course-slug class-id]
  (let [{course-id :id} (db/get-course {:slug course-slug})
        stats (->> (db/get-course-stats {:class_id class-id :course_id course-id})
                   (map class/with-user)
                   (map class/with-student-by-user))]
    [true {:stats stats
           :class-id class-id
           :course-name course-slug}]))

(defn workflow->grid
  [levels f]
  (let [->lesson (fn [lesson level] {:name (str "L" (:lesson lesson))
                                     :values (map #(f % level (:lesson lesson)) (:activities lesson))})
        ->level (fn [level] [(:level level) (map #(->lesson % (:level level)) (:lessons level))])]
    (->> levels
         (map ->level)
         (into {}))))

(defn- stats->hash
  [stats]
  (let [->activity (fn [activity] [(:activity activity) activity])
        ->lesson (fn [[lesson-id activities]] [lesson-id (->> activities
                                                              (map ->activity)
                                                              (into {}))])
        ->level (fn [[level-id lessons]] [level-id (->> lessons
                                                        (group-by :lesson)
                                                        (map ->lesson)
                                                        (into {}))])]
    (->> stats
         (map :data)
         (group-by :level)
         (map ->level)
         (into {}))))

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
  [stats]
  (let [hash (stats->hash stats)
        get-stat (fn [level lesson activity] (-> hash
                                                 (get level)
                                                 (get lesson)
                                                 (get activity)))]
    (fn [{:keys [activity scored]} level lesson]
      (let [data (get-stat level lesson activity)]
        {:label activity
         :started (boolean data)
         :finished (-> data :score boolean)
         :percentage (score->value (-> data :score) scored)
         :value (score->value (-> data :score) scored)}))))

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
  [stats]
  (let [hash (stats->hash stats)
        get-stat (fn [level lesson activity] (-> hash
                                                 (get level)
                                                 (get lesson)
                                                 (get activity)))]
    (fn [{:keys [activity time-expected]} level lesson]
      (let [data (get-stat level lesson activity)]
        {:label activity
         :started (boolean data)
         :finished (-> data :score boolean)
         :percentage (time->percentage (-> data :time-spent) time-expected)
         :value (time->value (-> data (:time-spent 0)))}))))

(defn get-individual-progress [course-slug student-id]
  (let [{user-id :user-id} (db/get-student {:id student-id})
        {course-id :id} (db/get-course {:slug course-slug})
        course-data (course/get-course-data course-slug)
        stats (db/get-user-activity-stats {:user_id user-id :course_id course-id})]
    [true {:stats stats
           :scores (workflow->grid (:levels course-data) (activity->score stats))
           :times (workflow->grid (:levels course-data) (activity->time stats))}]))

(defn save-events! [owner-id course-id events]
  (doseq [{created-at-string :created-at type :type :as data} events]
    (let [created-at (jt/offset-date-time created-at-string)]
      (db/create-event! {
                         :user_id owner-id
                         :course_id course-id
                         :created_at created-at
                         :type type
                         :guid (java.util.UUID/fromString (:id data))
                         :data data
                         })
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
  [owner-id course-slug {:keys [progress events]}]
  (let [{course-id :id} (db/get-course {:slug course-slug})]
    (save-events! owner-id course-id events)
    (if-let [{id :id} (db/get-progress {:user_id owner-id :course_id course-id})]
      (update-progress! id progress)
      (create-progress! owner-id course-id progress))))

(defn- levels->finished
  [levels level-id lesson-id]
  (let [->lesson (fn [lesson] [(:lesson lesson) (->> (:activities lesson) (map :activity) (into #{}))])
        ->level (fn [level] [(:level level) (->> (:lessons level) (map ->lesson) (into {}))])
        prepared (->> levels (map ->level) (into {}))]
    (if level-id
      (let [filtered-levels (->> prepared
                                 (keep (fn [[idx item]] (when (>= level-id idx) [idx item])))
                                 (into {}))]
        (if lesson-id
          (let [[last-level last-lessons] (last filtered-levels)
                filtered-lessons (->> last-lessons
                                      (keep (fn [[idx item]] (when (>= lesson-id idx) [idx item])))
                                      (into {}))]
            (-> filtered-levels
                (assoc last-level filtered-lessons)))
          filtered-levels))
      prepared)))

(defn next-not-finished
  [levels finished]
  (let [[level-num level] (apply max-key key finished)
        [lesson-num _] (apply max-key key level)
        level (first (filter #(= level-num (:level %)) levels))
        lesson (first (filter #(= lesson-num (:lesson %)) (:lessons level)))
        activity (:activity (last (:activities lesson)))]
    (activity/next-for levels {:level level-num :lesson lesson-num :activity activity})))

(defn complete-individual-progress! [course-slug student-id {lesson :lesson level :level}]
  (let [{user-id :user-id} (db/get-student {:id student-id})
        {course-id :id} (db/get-course {:slug course-slug})
        levels (-> (course/get-course-data course-slug) :levels)
        finished (-> levels
                     (levels->finished level lesson))
        next (next-not-finished levels finished)
        progress (->
                   (db/get-progress {:user_id user-id :course_id course-id})
                   :data
                   (assoc :finished finished)
                   (assoc :next next))]
    (save-progress! user-id course-slug {:progress progress})))
