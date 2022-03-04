(ns webchange.student-dashboard-v2.subs
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.lessons.activity :as lessons-activity]
    [webchange.progress.activity :as common-activity]))

(defn- scene-name->scene
  [scene-name scenes]
  (let [{:keys [name preview type]} (get scenes (keyword scene-name))]
    {:id    scene-name
     :type  type
     :name  name
     :image preview}))

(defn- activity-letter
  [db {:keys [level lesson] :as activity}]
  (if-let [lesson-set-name (get-in db [:course-data :levels level :lessons lesson :lesson-sets :concepts-single])]
    (let [item-id (-> (get-in db [:lessons lesson-set-name :item-ids])
                      first)
          letter (get-in db [:dataset-items item-id :data :letter])]
      (assoc activity :letter letter))
    activity))

(re-frame/reg-sub
  ::finished-activities
  (fn [db]
    (let [scenes (get-in db [:course-data :scene-list])
          activities (lessons-activity/flatten-activities (get-in db [:course-data :levels]))]
      (->> activities
           (filter #(lessons-activity/finished? db % activities))
           (map #(merge % (scene-name->scene (:activity-name %) scenes)))
           (map #(assoc % :completed true))
           (map #(activity-letter db %))))))

(re-frame/reg-sub
  ::next-activity
  (fn [db]
    (let [scenes (get-in db [:course-data :scene-list])
          next (lessons-activity/get-progress-next db)
          activity (-> (get-in db [:course-data :levels])
                       (get-in [(:level next) :lessons (:lesson next) :activities (:activity next)]))]
      (merge next (scene-name->scene (:activity activity) scenes)))))

(defn last-level-done [finished]
  (apply max (map #(js/parseInt (name %)) (keys finished))))

(defn last-lesson-done [finished level]
  (apply max (map #(js/parseInt (name %)) (keys ((keyword (str level)) finished)))))

(re-frame/reg-sub
  ::finished-level-lesson-activities
  (fn [db]
    (let [levels (get-in db [:course-data :levels])
          finished (get-in db [:progress-data :finished])
          last-level (last-level-done finished)
          last-lesson (last-lesson-done finished last-level)
          lesson-activities (get-in levels [last-level :lessons last-lesson :activities])
          all-activities (lessons-activity/flatten-activities levels)]
      (->> (get-in finished [last-level last-lesson])
           (map #(common-activity/activity-unique-id->index all-activities %))
           (map #(get lesson-activities %))
           (map :activity)))))
