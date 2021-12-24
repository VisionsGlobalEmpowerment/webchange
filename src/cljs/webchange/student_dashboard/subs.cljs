(ns webchange.student-dashboard.subs
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.lessons.activity :as lessons-activity]))

(defn- scene-name->scene [scene-name scenes]
  (let [{:keys [name preview type]} (get scenes (keyword scene-name))]
    {:id scene-name
     :type type
     :name name
     :image preview}))

(re-frame/reg-sub
  ::course-activities
  (fn [db]
    (let [scenes (get-in db [:course-data :scene-list])
          activities (lessons-activity/flatten-activities (get-in db [:course-data :levels]))]
      (->> activities
           (map #(merge % (scene-name->scene (:activity-name %) scenes)))))))

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
           (filter #(lessons-activity/finished? db %))
           (map #(merge % (scene-name->scene (:activity-name %) scenes)))
           (map #(assoc % :completed true))
           (map #(activity-letter db %))))))

(defn next-activity
  [db]
  (let [scenes (get-in db [:course-data :scene-list])
        next (get-in db [:progress-data :next])
        activity (-> (get-in db [:course-data :levels])
                     (get-in [(:level next) :lessons (:lesson next) :activities (:activity next)]))]
    (merge next (scene-name->scene (:activity activity) scenes))))

(re-frame/reg-sub
  ::next-activity
  next-activity)

(re-frame/reg-sub
  ::assessments
  (fn [db]
    (let [scenes (get-in db [:course-data :scene-list])
          activities (lessons-activity/flatten-activities (get-in db [:course-data :levels]))
          is-assessment? #(= "assessment" (:type %))]
      (->> activities
           (map #(merge % (scene-name->scene (:activity-name %) scenes)))
           (map #(if (lessons-activity/finished? db %) (assoc % :completed true) %))
           (filter is-assessment?)))))

(re-frame/reg-sub
  ::progress-loading
  (fn [db]
    (or
      (get-in db [:loading :load-course])
      (get-in db [:loading :load-progress]))))

(defn last-level-done [finished]
  (apply max (map #(int (name %)) (keys finished))))

(defn last-lesson-done [finished level]
  (apply max (map #(int (name %)) (keys ((keyword (str level)) finished)))))

(re-frame/reg-sub
  ::finished-level-lesson-activities
  (fn [db]
    (let [levels (get-in db [:course-data :levels])
          finished (get-in db [:progress-data :finished])
          last-level (last-level-done finished)
          last-lesson (last-lesson-done finished last-level)
          lesson-activities (get-in levels [last-level :lessons last-lesson :activities])]
      (->> (get-in finished [last-level last-lesson])
           (map #(get lesson-activities %))
           (map :activity)))))

(defn lesson-progress
  [db]
  (let [finished (get-in db [:progress-data :finished])
        last-level (last-level-done finished)
        last-lesson (last-lesson-done finished last-level)

        finished-activities (get-in finished [last-level last-lesson])
        activities (lessons-activity/get-activities db last-level last-lesson)]
    (/ (count finished-activities)
       (count activities))))

(re-frame/reg-sub
  ::overall-progress
  (fn []
    [(re-frame/subscribe [::course-activities])
     (re-frame/subscribe [::finished-activities])])
  (fn [[course-activities finished-activities]]
    (/ (count finished-activities)
       (count course-activities))))
