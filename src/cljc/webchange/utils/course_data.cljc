(ns webchange.utils.course-data)

(defn get-levels-data
  [course-data]
  (get course-data :levels []))

(defn get-level-data
  [course-data level-idx]
  (-> (get-levels-data course-data)
      (nth level-idx nil)))

(defn get-lessons-data
  ([course-data level-idx]
   (-> (get-level-data course-data level-idx)
       (get-lessons-data)))
  ([level-data]
   (get level-data :lessons [])))

(defn get-lesson-data
  [course-data level-idx lesson-idx]
  (-> (get-lessons-data course-data level-idx)
      (nth lesson-idx nil)))

(defn get-activities-data
  ([course-data level-idx lesson-idx]
   (-> (get-lesson-data course-data level-idx lesson-idx)
       (get-activities-data)))
  ([lesson-data]
   (get lesson-data :activities [])))

(defn get-activity-info
  [course-data activity-name]
  (-> (get course-data :scene-list)
      (get (keyword activity-name))))
