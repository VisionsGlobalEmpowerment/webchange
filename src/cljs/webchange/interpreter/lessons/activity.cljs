(ns webchange.interpreter.lessons.activity
  (:require
    [webchange.progress.activity :as activity]
    [re-frame.core :as re-frame]))

(defn- get-activity
  [activities activity]
  (activity/first-by-key activities :activity activity))

(defn get-activities
  [db level lesson]
  (let [levels (get-in db [:course-data :levels])
        lessons (-> (activity/get-level levels level) :lessons)
        activities (-> (activity/get-lesson lessons lesson) :activities)]
    activities
  ))

(defn workflow-action
  [db {:keys [level lesson activity]}]
  (let [levels (get-in db [:course-data :levels])
        lessons (-> (activity/get-level levels level) :lessons)
        activities (-> (activity/get-lesson lessons lesson) :activities)]
    (get-activity activities activity)))

(defn- num->keyword
  [n]
  (-> n str keyword))

(defn finished?
  [db {:keys [level lesson activity]}]
  (let [activities (get-in db [:progress-data :finished (num->keyword level) (num->keyword lesson)])]
    (some #(= activity %) activities)))

(defn next-not-finished-for
  [db activity]
  (let [levels (get-in db [:course-data :levels])]
    (loop [next (activity/next-for levels activity)]
      (if (finished? db next)
        (recur (activity/next-for levels next))
        next))))

(defn finish
  [db {:keys [level lesson activity] :as finished}]
  (let [next (next-not-finished-for db finished)]
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
  (let [default {:level 1 :lesson 1}
        current (get-in db [:loaded-activity])
        next (get-in db [:progress-data :next])]
    (merge default next current {:activity scene-name})))

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
