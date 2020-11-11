(ns webchange.secondary.guid)

(defn guid-from-teacher [teacher]
  (str (:user-id teacher) "-" (:school-id teacher)))

(defn guid-from-student [student]
  (str (:user-id student)))

(defn guid-from-course-stats [course-stats]
  (str (:user-id course-stats) "-" (:class-id course-stats) "-" (:course-id course-stats)))

(defn guid-from-course-progresses [course-progresses]
  (str (:user-id course-progresses) "-" (:course-id course-progresses)))

(defn guid-from-course-events [course-events]
  (:guid course-events))

(defn guid-from-activity-stats [activity-stats]
  (str (:user-id activity-stats) "-" (:course-id activity-stats) "-" (:activity-at activity-stats)))

(defn guid-from-scene-skill [scene-skill]
  (str (:course-id scene-skill) "-" (:skill-id scene-skill)))

(defn guid-from-class [class]
  (:guid class))

(defn guid-from-courses [school]
  (:id school))

(defn guid-is-id [entry]
  (:id entry))

(defn guids-from-entries [entries guid]
  (vec (set (map (fn [entry] (guid entry)) entries))))
