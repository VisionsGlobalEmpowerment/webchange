(ns webchange.progress.finish
  (:require
    [webchange.progress.course-progress :refer [course-data->progress]]
    [webchange.progress.utils :refer [idx->keyword]]
    [webchange.utils.log :refer [log]]))

(defn- filter-last-finished-levels
  [level-idx finished]
  (-> (inc level-idx)
      (take finished)
      (vec)))

(defn- filter-last-finished-lessons
  [lesson-idx finished]
  (let [[last-level-id last-level-lessons] (last finished)
        rest-levels (-> finished drop-last vec)

        filtered-lessons (-> (inc lesson-idx)
                             (take last-level-lessons)
                             (vec))]
    (-> rest-levels
        (conj [last-level-id filtered-lessons])
        (vec))))

(defn- filter-last-finished-activities
  [activity-idx finished]
  (let [[last-level-id last-level-lessons] (last finished)
        rest-levels (-> finished drop-last vec)

        [last-lesson-id last-lesson-activities] (last last-level-lessons)
        rest-lessons (-> last-level-lessons drop-last vec)

        filtered-activities (-> (inc activity-idx)
                                (take last-lesson-activities)
                                (vec))]
    (-> rest-levels
        (conj [last-level-id
               (-> rest-lessons
                   (conj [last-lesson-id filtered-activities])
                   (vec))])
        (vec))))

(defn- ->sorted-map
  [l]
  (->> l
       (reduce concat [])
       (apply sorted-map)))

(defn- ->prepare-format
  [progress-data]
  (->> progress-data
       (map (fn [[level-id lessons]]
              [level-id (->sorted-map lessons)]))
       (->sorted-map)))

(defn get-finished-progress
  ([course-data]
   (get-finished-progress course-data {}))
  ([course-data {:keys [level-idx lesson-idx activity-idx]}]
   (cond->> (course-data->progress course-data)
            (some? level-idx) (filter-last-finished-levels level-idx)
            (some? lesson-idx) (filter-last-finished-lessons lesson-idx)
            (some? activity-idx) (filter-last-finished-activities activity-idx)
            :always (->prepare-format))))

(defn activity-finished?
  [current-progress {:keys [level-idx lesson-idx activity-id activity-idx]}]
  (-> (if-let [level (->> (idx->keyword level-idx)
                          (get current-progress))]
        (if-let [lesson (->> (idx->keyword lesson-idx)
                             (get level))]
          (cond
            (some? activity-id) (some #{activity-id} lesson)
            (some? activity-idx) (< activity-idx (count lesson)))))
      (boolean)))

(defn course-finished?
  [course-data current-progress]
  (let [finished-progress (get-finished-progress course-data)
        unfinished-activities (->> finished-progress
                                   (map (fn [[level-idx level]]
                                          (map (fn [[lesson-idx lesson]]
                                                 (map (fn [activity-id]
                                                        {:level-idx   level-idx
                                                         :lesson-idx  lesson-idx
                                                         :activity-id activity-id})
                                                      lesson))
                                               level)))
                                   (flatten)
                                   (filter #(not (activity-finished? current-progress %))))]
    (empty? unfinished-activities)))
