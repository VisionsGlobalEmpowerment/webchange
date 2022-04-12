(ns webchange.progress.activity
  (:require
    [webchange.progress.tags :as tags]
    [webchange.utils.log :refer [log]]))

(defn activity-data-by-index
  [levels level lesson activity]
  (let [data (get-in levels [level :lessons lesson :activities activity])]
    {:level         level
     :lesson        lesson
     :activity      activity
     :unique-id     (:unique-id data)
     :activity-name (:activity data)}))

(defn- indices
  [pred coll]
  (keep-indexed #(when (pred %2) %1) coll))

(defn- index-by-key
  [coll key value]
  (first (indices #(= value (get % key)) coll)))

(defn- not-last?
  [coll index]
  (> (count coll) (inc index)))

(defn first-by-key
  [coll key value]
  (reduce #(when (= value (get %2 key)) (reduced %2)) nil coll))

(defn get-level
  [levels level]
  (first-by-key levels :level level))

(defn get-lesson
  [lessons lesson]
  (first-by-key lessons :lesson lesson))

(defn- num->keyword
  [n]
  (-> n str keyword))

(defn finish
  "Add given activity (level index, lesson index, activity index)
  into finished map from students progress"
  [finished {:keys [level lesson unique-id] :as activity}]
  (update-in finished [(num->keyword level) (num->keyword lesson)] #(-> % set (conj unique-id))))

(defn activity-unique-id->index [activities unique-id]
  (->> activities
    (filter #(= (:unique-id %) unique-id))
    first
    :activity))

(defn finished?
  "Check if given activity (level index, lesson index, activity index)
  is in finished map from students progress"
  [finished {:keys [level lesson activity]} activities]
  (let [finished-unique-ids (get-in finished [(num->keyword level) (num->keyword lesson)])
        finished-indices (map #(activity-unique-id->index activities %) finished-unique-ids)]
    (if (empty? finished-indices)
      false
      (<= activity (apply max finished-indices)))))

(defn- flatten-activity
  [level-idx lesson-idx activity-idx activity]
  (let [activity-name (:activity activity)]
    (assoc activity :level level-idx :lesson lesson-idx :activity activity-idx :activity-name activity-name)))

(defn- flatten-lesson
  [level-idx lesson-idx lesson]
  (map-indexed (fn [activity-idx activity] (flatten-activity level-idx lesson-idx activity-idx activity)) (:activities lesson)))

(defn- flatten-level
  [level-idx level]
  (map-indexed (fn [lesson-idx lesson] (flatten-lesson level-idx lesson-idx lesson)) (:lessons level)))

(defn flatten-activities
  [levels]
  (->> levels
       (map-indexed flatten-level)
       flatten))

(defn- excluded-by-tags?
  [tags levels {:keys [level lesson activity]}]
  (let [workflow-action (get-in levels [level :lessons lesson :activities activity])]
    (and
     (:only workflow-action)
     (not (tags/has-one-from tags (:only workflow-action))))))

(defn next-for
  [levels {:keys [level lesson activity]}]
  (let [level (or level 0)
        lesson (or lesson 0)
        activity (or activity 0)
        lessons (get-in levels [level :lessons])
        activities (get-in lessons [lesson :activities])]
    (cond
      (not-last? activities activity) (activity-data-by-index levels level lesson (inc activity))
      (not-last? lessons lesson) (activity-data-by-index levels level (inc lesson) 0)
      (not-last? levels level) (activity-data-by-index levels (inc level) 0 0))))

(defn next-not-finished-for
  "Return next activity in course levels that is
  - not finished yet
  - satisfies provided tags"
  [tags levels finished activity]
  (let [activities (flatten-activities levels)]
    (loop [next (next-for levels activity)
           cur activity]
      (cond
        (nil? next) cur
        (or (finished? finished next activities)
            (excluded-by-tags? tags levels next)) (recur (next-for levels next)
                                                         next)
        :else next))))


