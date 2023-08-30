(ns webchange.utils.course-data
  (:require
    [webchange.utils.list :as l]))

(def empty-level-data {:lessons []})
(def empty-lesson-data {:activities []})

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

(defn next-uid
  [course-data]
  (->> (get-levels-data course-data)
       (map get-lessons-data)
       (flatten)
       (map get-activities-data)
       (flatten)
       (map :unique-id)
       (sort)
       (last)
       (inc)))

(defn- level-idx?
  [value]
  (number? value))

(defn- lesson-idx?
  [value]
  (number? value))

(defn- activity-idx?
  [value]
  (number? value))

(defn- activity-slug?
  [value]
  (or (string? value)
      (nil? value)))

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
  (let [target-level (cond-> target-level
                             (> target-level source-level) (dec)
                             (= position :after) (inc)
                             :to-index-0 (dec))
        source-level (dec source-level)]
    (update course-data :levels l/move-item source-level target-level)))

(defn remove-level
  [course-data level-idx]
  {:pre [(level-idx? level-idx)]}
  (update course-data :levels l/remove-at-position (dec level-idx)))

(defn add-lesson
  ([course-data params]
   (add-lesson course-data params {}))
  ([course-data {:keys [target-level target-lesson position]} lesson-data]
   {:pre [(level-idx? target-level)
          (lesson-idx? target-lesson)
          (position? position)]}
   (update course-data :levels l/update-nth (dec target-level)
           update :lessons l/insert-at-position
           (merge empty-lesson-data lesson-data)
           (get-item-index target-lesson position))))

(defn remove-lesson
  [course-data level-idx lesson-idx]
  {:pre [(level-idx? level-idx)
         (lesson-idx? lesson-idx)]}
  (update-in course-data [:levels (dec level-idx) :lessons] l/remove-at-position (dec lesson-idx)))

(defn move-lesson
  [course-data {:keys [source-level source-lesson target-level target-lesson position]}]
  {:pre [(level-idx? source-level)
         (lesson-idx? source-lesson)
         (level-idx? target-level)
         (lesson-idx? target-lesson)
         (position? position)]}
  (let [moved-lesson (get-in course-data [:levels (dec source-level) :lessons (dec source-lesson)])
        course-without-lesson (remove-lesson course-data source-level source-lesson)
        lesson-position (if (and (= source-level target-level)
                                 (< source-lesson target-lesson))
                          (dec target-lesson)
                          target-lesson)]
    (add-lesson course-without-lesson {:target-level  target-level
                                       :target-lesson lesson-position
                                       :position      position}
                moved-lesson)))

(defn add-activity
  ([course-data params]
   (add-activity course-data params {}))
  ([course-data {:keys [activity-slug target-level target-lesson target-activity position]} activity-data]
   {:pre [(activity-slug? activity-slug)
          (level-idx? target-level)
          (lesson-idx? target-lesson)
          (activity-idx? target-activity)
          (position? position)]}
   (update course-data :levels l/update-nth (dec target-level)
           update :lessons l/update-nth (dec target-lesson)
           update :activities l/insert-at-position
           (merge {:activity  activity-slug
                   :unique-id (next-uid course-data)}
                  activity-data)
           (get-item-index target-activity position))))

(defn edit-placeholder
  [course-data {:keys [level-idx lesson-idx activity-idx name]}]
  (assoc-in course-data
            [:levels (dec level-idx) :lessons (dec lesson-idx) :activities (dec activity-idx) :name]
            name))

(defn remove-activity
  [course-data level-idx lesson-idx activity-idx]
  {:pre [(level-idx? level-idx)
         (lesson-idx? lesson-idx)
         (activity-idx? activity-idx)]}
  (update-in course-data
             [:levels (dec level-idx) :lessons (dec lesson-idx) :activities]
             l/remove-at-position
             (dec activity-idx)))

(defn move-activity
  [course-data {:keys [source-level source-lesson source-activity target-level target-lesson target-activity position]}]
  {:pre [(level-idx? source-level)
         (lesson-idx? source-lesson)
         (activity-idx? source-activity)
         (level-idx? target-level)
         (lesson-idx? target-lesson)
         (activity-idx? target-activity)
         (position? position)]}
  (let [moved-activity (get-in course-data [:levels (dec source-level) :lessons (dec source-lesson) :activities (dec source-activity)])
        course-without-activity (remove-activity course-data source-level source-lesson source-activity)
        activity-position (if (and (= source-level target-level)
                                   (= source-lesson target-lesson)
                                   (< source-activity target-activity))
                            (dec target-activity)
                            target-activity)]
    (add-activity course-without-activity {:target-level    target-level
                                           :target-lesson   target-lesson
                                           :target-activity activity-position
                                           :position        position}
                  moved-activity)))

(defn get-activity
  [course-data {:keys [target-level target-lesson target-activity]}]
  {:pre [(level-idx? target-level)
         (lesson-idx? target-lesson)
         (activity-idx? target-activity)]}
  (get-in course-data [:levels (dec target-level) :lessons (dec target-lesson) :activities (dec target-activity)]))

(defn replace-activity
  ([course-data params]
   (add-activity course-data params {}))
  ([course-data {:keys [activity-slug target-level target-lesson target-activity position]} activity-data]
   {:pre [(activity-slug? activity-slug)
          (level-idx? target-level)
          (lesson-idx? target-lesson)
          (activity-idx? target-activity)
          (position? position)]}
   (let [activities (-> (get-in course-data [:levels (dec target-level) :lessons (dec target-lesson) :activities])
                        (l/remove-at-position (dec target-activity))
                        (l/insert-at-position (merge {:activity  activity-slug
                                                      :unique-id (next-uid course-data)}
                                                     activity-data)
                                              (dec target-activity)))]
     (assoc-in course-data [:levels (dec target-level) :lessons (dec target-lesson) :activities] activities))))
