(ns webchange.progress.finish
  (:require
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

(defn- ->lesson
  [idx lesson]
  [idx (->> (:activities lesson)
            (map :unique-id)
            (vec))])

(defn- ->level
  [idx level]
  [idx (->> (:lessons level)
            (map-indexed ->lesson)
            (vec))])

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
  [course-data level-idx lesson-idx activity-idx]
  (let [{:keys [levels]} course-data]
    (cond->> (map-indexed ->level levels)
             level-idx (filter-last-finished-levels level-idx)
             lesson-idx (filter-last-finished-lessons lesson-idx)
             activity-idx (filter-last-finished-activities activity-idx)
             :always (->prepare-format))))
