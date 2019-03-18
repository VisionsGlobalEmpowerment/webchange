(ns webchange.progress.core
  (:require [webchange.db.core :refer [*db*] :as db]
            [clojure.tools.logging :as log]
            [camel-snake-kebab.extras :refer [transform-keys]]
            [camel-snake-kebab.core :refer [->snake_case_keyword]]
            [webchange.auth.core :as auth]
            [webchange.events :as events]
            [java-time :as jt]))

(defn get-current-progress [course-name student-id]
  (let [{course-id :id} (db/get-course {:name course-name})
        progress (db/get-progress {:user_id student-id :course_id course-id})]
    [true {:progress progress}]))

(defn get-class-profile [course-id class-id]
  (let [stats (db/get-course-stats {:class_id class-id :course_id course-id})]
    [true {:stats stats}]))

(defn get-individual-progress [course-id student-id]
  (let [stats (db/get-activity-stats {:user_id student-id :course_id course-id})]
    [true {:stats stats}]))

(defn save-actions! [owner-id course-id actions]
  (doseq [{created-at-string :created-at type :type :as data} actions]
    (let [created-at (jt/local-date-time created-at-string)]
      (db/create-action! {:user_id owner-id :course_id course-id :created_at created-at :type type :data data})
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
  [owner-id course-name {:keys [progress actions]}]
  (let [{course-id :id} (db/get-course {:name course-name})]
    (save-actions! owner-id course-id actions)
    (if-let [{id :id} (db/get-progress {:user_id owner-id :course_id course-id})]
      (update-progress! id progress)
      (create-progress! owner-id course-id progress))))
