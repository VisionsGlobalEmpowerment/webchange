(ns webchange.progress.activity
  (:require
    [webchange.progress.tags :as tags]))

(defn activity-data-by-index
  [levels level lesson activity]
  (merge (get-in levels [level :lessons lesson :activities activity])
         {:level (get-in levels [level :level])
          :lesson (get-in levels [level :lessons lesson :lesson])}))

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

(defn next-for
  [current-tags levels {:keys [level lesson activity]}]
  (let [level-index (or (index-by-key levels :level level) 0)
        lessons (-> (get-level levels level) :lessons)
        lesson-index (or (index-by-key lessons :lesson lesson) 0)
        activities (-> (get-lesson lessons lesson) :activities)
        activity-index (index-by-key activities :activity activity)
        levels (assoc-in levels [level-index :lessons lesson-index :activities]
                          (vec (filter some? (map-indexed
                                          (fn [idx act]
                                            (if (<= idx activity-index)
                                              act
                                              (if (:only act)
                                                (if (tags/has-one-from current-tags (:only act)) act nil)
                                                act)
                                            )) activities))))
        activities (get-in levels [level-index :lessons lesson-index :activities])
        activity-index (index-by-key activities :activity activity)]
    (cond
      (not-last? activities activity-index) (activity-data-by-index levels level-index lesson-index (inc activity-index))
      (not-last? lessons lesson-index) (activity-data-by-index levels level-index (inc lesson-index) 0)
      (not-last? levels level-index) (activity-data-by-index levels (inc level-index) 0 0))))

