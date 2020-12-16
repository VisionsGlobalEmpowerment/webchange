(ns webchange.interpreter.lessons.activity
  (:require
    [webchange.progress.activity :as activity]))

(defn get-activities
  [db level lesson]
  (get-in db [:course-data :levels level :lessons lesson :activities]))

(defn workflow-action
  [db {:keys [level lesson activity]}]
  (get-in db [:course-data :levels level :lessons lesson :activities activity]))

(defn finished?
  [db activity]
  (let [finished (get-in db [:progress-data :finished])]
    (activity/finished? finished activity)))

(defn next-not-finished-for
  [db activity]
  (let [current-tags (get-in db [:progress-data :current-tags] [])
        levels (get-in db [:course-data :levels])
        finished (get-in db [:progress-data :finished])]
    (activity/next-not-finished-for current-tags levels finished activity)))

(defn finish
  [db finished]
  (let [next (next-not-finished-for db finished)]
    (-> db
        (update-in [:progress-data :finished] activity/finish finished)
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

(defn- workflow-action-idx
  "Return activity if its correct index
  otherwise find index in course workflow by activity-name"
  [db {:keys [level lesson activity activity-name]}]
  (let [activities (get-in db [:course-data :levels level :lessons lesson :activities])
        workflow-action (get activities activity)]
    (if (= activity-name (:activity workflow-action))
      activity
      (-> (keep-indexed #(when (= (:activity %2) activity-name) %1) activities)
          first))))

;TODO: level what if scene is not available in current level/lesson?
(defn name->activity-action
  [db scene-name]
  (let [default {:level 0 :lesson 0 :activity 0}
        current (get-in db [:loaded-activity])
        next (get-in db [:progress-data :next])
        activity-action (merge default next current {:activity-name scene-name})
        activity-idx (workflow-action-idx db activity-action)]
    (assoc activity-action :activity activity-idx)))

(defn clear-loaded-activity
  [db]
  (dissoc db :loaded-activity))

(defn add-loaded-activity
  [db activity]
  (assoc db :loaded-activity activity))

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
