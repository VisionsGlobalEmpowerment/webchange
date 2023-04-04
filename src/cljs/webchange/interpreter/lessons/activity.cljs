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
  [db activity activities]
  (let [finished (get-in db [:progress-data :finished])]
    (activity/finished? finished activity activities)))

(defn new?
  [db activity activities]
  (let [finished (get-in db [:progress-data :finished])]
    (activity/new? finished activity activities)))

(defn next-not-finished-for
  [db activity]
  (let [current-tags (get-in db [:progress-data :current-tags] [])
        levels (get-in db [:course-data :levels])
        finished (get-in db [:progress-data :finished])]
    (activity/next-not-finished-for current-tags levels finished activity)))

(defn finish
  [db finished]
  (update-in db [:progress-data :finished] activity/finish finished))

(defn activity-progress
  [db]
  (let [finished (get-in db [:progress-data :finished])]
    (->> finished
         vals
         (mapcat vals)
         (map count)
         (reduce +))))

(defn- workflow-action-idx
  "Return activity if its correct index
  otherwise find index in course workflow by activity-name"
  [db {:keys [level lesson activity scene-id]}]
  (let [activities (get-in db [:course-data :levels level :lessons lesson :activities])
        workflow-action (get activities activity)]
    (if (= scene-id (:scene-id workflow-action))
      activity
      (-> (keep-indexed #(when (= (:scene-id %2) scene-id) %1) activities)
          first))))

(declare get-progress-next)

(defn current-activity-action
  [db]
  (let [default {:level 0 :lesson 0 :activity 0}
        current (get-in db [:loaded-activity])
        next (get-progress-next db)
        activity-action (merge default next current)
        activity-idx (workflow-action-idx db activity-action)]
    (assoc activity-action :activity activity-idx)))

(defn clear-loaded-activity
  [db]
  (dissoc db :loaded-activity))

(defn add-loaded-activity
  [db activity]
  (assoc db :loaded-activity activity))

(defn get-progress-next [db]
  (let [activities (activity/flatten-activities (get-in db [:course-data :levels]))
        finished-activities (filter #(finished? db % activities) activities)]
    (if (empty? finished-activities)
      (first activities)
      (next-not-finished-for db (last finished-activities)))))
