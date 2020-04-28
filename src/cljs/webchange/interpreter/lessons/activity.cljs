(ns webchange.interpreter.lessons.activity
  (:require
    [re-frame.core :as re-frame]))

(defn- indices
  [pred coll]
  (keep-indexed #(when (pred %2) %1) coll))

(defn- index-by-key
  [coll key value]
  (first (indices #(= value (get % key)) coll)))

(defn- first-by-key
  [coll key value]
  (reduce #(when (= value (get %2 key)) (reduced %2)) nil coll))

(defn- get-level
  [levels level]
  (first-by-key levels :level level))

(defn- get-lesson
  [lessons lesson]
  (first-by-key lessons :lesson lesson))

(defn- get-activity
  [activities activity]
  (first-by-key activities :activity activity))

(defn workflow-action
  [db {:keys [level lesson activity]}]
  (let [levels (get-in db [:course-data :levels])
        lessons (-> (get-level levels level) :lessons)
        activities (-> (get-lesson lessons lesson) :activities)]
    (get-activity activities activity)))

(defn- activity-data-by-index
  [levels level lesson activity]
  (merge (get-in levels [level :lessons lesson :activities activity])
         {:level (get-in levels [level :level])
         :lesson (get-in levels [level :lessons lesson :lesson])}
    )
  )

(defn- not-last?
  [coll index]
  (> (count coll) (inc index)))

(defn next-for
  [db {:keys [level lesson activity]}]
  (let [levels (get-in db [:course-data :levels])
        level-index (index-by-key levels :level level)
        lessons (-> (get-level levels level) :lessons)
        lesson-index (index-by-key lessons :lesson lesson)
        activities (-> (get-lesson lessons lesson) :activities)
        activity-index (index-by-key activities :activity activity)]
    (cond
      (not-last? activities activity-index) (activity-data-by-index levels level-index lesson-index (inc activity-index))
      (not-last? lessons lesson-index) (activity-data-by-index levels level-index (inc lesson-index) 0)
      (not-last? levels level-index) (activity-data-by-index levels (inc level-index) 0 0))))

(defn- num->keyword
  [n]
  (-> n str keyword))

(defn finished?
  [db {:keys [level lesson activity]}]
  (let [activities (get-in db [:progress-data :finished (num->keyword level) (num->keyword lesson)])]
    (some #(= activity %) activities)))

(defn finish
  [db {:keys [level lesson activity] :as finished}]
  (let [next (next-for db finished)]
    (-> db
        (update-in [:progress-data :finished (num->keyword level) (num->keyword lesson)] #(-> % set (conj activity)))
        (assoc-in [:progress-data :next] next))))

(defn activity-progress
  [db]
  (let [finished (get-in db [:progress-data :finished])]
    (->> finished
         (map second)
         (map vals)
         flatten
         (map count)
         (reduce +))))

;TODO: level what if scene is not available in current level/lesson?
(defn name->activity-action
  [db scene-name]
  (let [current (get-in db [:loaded-activity])
        next (get-in db [:progress-data :next])]
    (merge next current {:activity scene-name})))

(defn clear-loaded-activity
  [db]
  (dissoc db :loaded-activity))

(defn add-loaded-activity
  [db activity]
  (assoc db :loaded-activity activity))

(defn- flatten-activity
  [level lesson activity]
  (assoc activity :level (:level level) :lesson (:lesson lesson)))

(defn- flatten-lesson
  [level lesson]
  (map #(flatten-activity level lesson %) (:activities lesson)))

(defn- flatten-level
  [level]
  (map #(flatten-lesson level %) (:lessons level)))

(defn flatten-activities
  [levels]
  (->> levels
       (map flatten-level)
       flatten))
