(ns webchange.utils.course-data
  (:require
    [webchange.utils.list :as l]))

(def empty-level-data {:lessons []})

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

(defn get-levels-count
  [course-data]
  (->> (get-levels-data course-data)
       (count)))

(defn get-lessons-count
  [course-data]
  (->> (get-levels-data course-data)
       (map get-lessons-data)
       (map count)
       (apply +)))

(defn get-activities-count
  [course-data]
  (->> (get-levels-data course-data)
       (map (fn [level-data]
              (->> (get-lessons-data level-data)
                   (map (fn [lesson-data]
                          (->> (get-activities-data lesson-data)
                               (count))))
                   (apply +))))
       (apply +)))

(defn- level-idx?
  [value]
  (number? value))

(defn- lesson-idx?
  [value]
  (number? value))

(defn- activity-id?
  [value]
  (number? value))

(defn- activity-name?
  [value]
  (string? value))

(defn- position?
  [value]
  (some #{value} [:after :before]))

(defn- get-item-index
  [passed-index relative-position]
  (if (= relative-position :before)
    (dec passed-index)
    passed-index))

(defn add-level
  ([course-data params]
   (add-level course-data params {}))
  ([course-data {:keys [target-level position]} level-data]
   {:pre [(level-idx? target-level)
          (position? position)]}
   (->> (get-item-index target-level position)
        (update course-data :levels l/insert-at-position (merge empty-level-data level-data)))))

(defn move-level
  [course-data {:keys [source-level target-level position]}]
  {:pre [(level-idx? source-level)
         (level-idx? target-level)
         (position? position)]}
  (let [source-level (dec source-level)
        target-level (cond-> target-level
                             (> target-level source-level) (dec)
                             (= position :after) (inc)
                             :to-index-0 (dec))]
    (update course-data :levels l/move-item source-level target-level)))

(defn remove-level
  [course-data level-idx]
  {:pre [(level-idx? level-idx)]}
  (update course-data :levels l/remove-at-position (dec level-idx)))

(defn add-lesson
  [course-data {:keys [target-level target-lesson position]}]
  {:pre [(level-idx? target-level)
         (lesson-idx? target-lesson)
         (position? position)]}
  course-data)

(defn move-lesson
  [course-data {:keys [source-level source-lesson target-level target-lesson position]}]
  {:pre [(level-idx? source-level)
         (lesson-idx? source-lesson)
         (level-idx? target-level)
         (lesson-idx? target-lesson)
         (position? position)]}
  course-data)

(defn remove-lesson
  [course-data level-idx lesson-idx]
  {:pre [(level-idx? level-idx)
         (lesson-idx? lesson-idx)]}
  course-data)

(defn add-activity
  [course-data {:keys [activity-id target-level target-lesson target-activity position]}]
  {:pre [(activity-name? activity-id)
         (level-idx? target-level)
         (lesson-idx? target-lesson)
         (activity-id? target-activity)
         (position? position)]}
  course-data)

(defn move-activity
  [course-data {:keys [source-level source-lesson source-activity target-level target-lesson target-activity position]}]
  {:pre [(level-idx? source-level)
         (lesson-idx? source-lesson)
         (activity-id? source-activity)
         (level-idx? target-level)
         (lesson-idx? target-lesson)
         (activity-id? target-activity)
         (position? position)]}
  course-data)

(defn remove-activity
  [course-data level-idx lesson-idx activity-idx]
  {:pre [(level-idx? level-idx)
         (lesson-idx? lesson-idx)
         (activity-id? activity-idx)]}
  course-data)
