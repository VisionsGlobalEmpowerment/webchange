(ns webchange.progress.activity
  (:require
    [webchange.progress.tags :as tags]))

(defn activity-data-by-index
  [levels level lesson activity]
  (let [{activity-name :activity} (get-in levels [level :lessons lesson :activities activity])]
    {:level         level
     :lesson        lesson
     :activity      activity
     :activity-name activity-name}))

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
  [finished {:keys [level lesson activity]}]
  (update-in finished [(num->keyword level) (num->keyword lesson)] #(-> % set (conj activity))))

(defn finished?
  "Check if given activity (level index, lesson index, activity index)
  is in finished map from students progress"
  [finished {:keys [level lesson activity]}]
  (let [activities (get-in finished [(num->keyword level) (num->keyword lesson)])]
    (some #(= activity %) activities)))

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
  (loop [next (next-for levels activity)]
    (if (or (finished? finished next) (excluded-by-tags? tags levels next))
      (recur (next-for levels next))
      next)))
