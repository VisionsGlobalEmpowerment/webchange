(ns webchange.progress.core
  (:require [webchange.db.core :refer [*db*] :as db]
            [clojure.tools.logging :as log]
            [webchange.auth.core :as auth]
            [webchange.progress.activity :as activity]
            [webchange.events :as events]
            [webchange.progress.tags :as tags]
            [webchange.class.core :as class]
            [webchange.course.core :as course]
            [webchange.progress.finish :refer [get-finished-progress]]
            [java-time :as jt]
            [clojure.string :as str]))

(defn update-default-tags
  [user-id progress]
  (let [
        current-tags (get-in progress [:data :current-tags])
        {date-of-birth :date-of-birth} (db/get-student-by-user {:user_id user-id})
        now (jt/local-date)
        age (if date-of-birth
              (jt/time-between date-of-birth now :years)
              0)
        age-tag (if (<= 4 age) [tags/age-above-or-equal-4] [tags/age-less-4])
        level-tag (if (not (tags/has-one-from tags/learning-level-tags current-tags)) [tags/advanced] [])
        current-tags (-> current-tags
                         (tags/remove-tags tags/age-tags)
                         (concat age-tag)
                         (concat level-tag))]
    (assoc-in progress [:data :current-tags] (vec (set current-tags)))))

(defn get-current-progress [course-slug student-id]
  (let [{course-id :id} (db/get-course {:slug course-slug})
        progress (->> (db/get-progress {:user_id student-id :course_id course-id})
                      (update-default-tags student-id))]
    [true {:progress (:data progress)}]))

(defn get-class-profile [course-slug class-id]
  (let [{course-id :id} (db/get-course {:slug course-slug})
        activities-count (-> (course/get-course-data course-slug)
                             (get :levels)
                             (activity/flatten-activities)
                             (count))
        stats (->> (db/get-course-stats {:class_id class-id :course_id course-id})
                   (map class/with-user)
                   (map class/with-student-by-user))]
    [true {:stats                    stats
           :class-id                 class-id
           :course-name              course-slug
           :course-activities-number activities-count}]))

(defn get-class-students-progress
  [class-id]
  (let [students (db/get-students-by-class {:class_id class-id})
        progress (db/get-class-students-progress {:class_id class-id})]
    {:students (->> students (map class/with-user))
     :progress progress}))

(defn get-class-student-progress
  [student-id]
  (let [student (db/get-student {:id student-id})
        class (db/get-class {:id (:class-id student)})
        course (db/get-course {:slug (:course-slug class)})
        course-latest (db/get-latest-course-version {:course_id (:course-id class)})
        course-stats (db/get-user-course-stat {:user_id (:user-id student) :course_id (:course-id class)})
        activity-stats (db/get-user-activity-stats {:user_id (:user-id student) :course_id (:course-id class)})]
    {:student (-> student
                  class/with-user)
     :class class
     :course (merge course-latest course)
     :course-stats course-stats
     :activity-stats activity-stats}))

(defn save-events! [owner-id course-id events]
  (doseq [{created-at-string :created-at type :type :as data} events]
    (let [created-at (jt/offset-date-time created-at-string)]
      (db/create-event! {
                         :user_id    owner-id
                         :course_id  course-id
                         :created_at created-at
                         :type       type
                         :guid       (java.util.UUID/fromString (:id data))
                         :data       data
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

(defn complete-individual-progress!
  [course-slug student-id {lesson-val :lesson level-val :level activity-val :activity navigation :navigation}]
  (let [level (when level-val (dec level-val))
        lesson (when lesson-val (dec lesson-val))
        activity (when activity-val (dec activity-val))
        {user-id :user-id} (db/get-student {:id student-id})
        {course-id :id} (db/get-course {:slug course-slug})
        course-data (course/get-course-data course-slug)
        levels (get course-data :levels)
        finished (get-finished-progress course-data {:level-idx    level
                                                     :lesson-idx   lesson
                                                     :activity-idx activity})
        current-tags (-> (db/get-progress {:user_id user-id :course_id course-id})
                         :data
                         :current-tags)
        next (activity/next-not-finished-for current-tags levels finished {:level level :lesson lesson :activity activity})
        progress (-> (db/get-progress {:user_id user-id :course_id course-id})
                     :data
                     (assoc :finished finished)
                     (assoc :next next)
                     (assoc :user-mode (when navigation "game-with-nav")))]
    (save-progress! user-id course-slug {:progress progress})
    (reduce #(do (log/debug "finished" %2)
                 (events/dispatch {:user-id user-id
                                   :course-id course-id
                                   :created-at    (jt/format (jt/offset-date-time))
                                   :type "activity-finished"
                                   :unique-id %2
                                   :time-spent 0})) nil (->> finished
                                                             (mapcat second)
                                                             (mapcat second)))
    progress))
