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
           (map #(merge % (scene-name->scene (:activity %) scenes)))))))

(re-frame/reg-sub
  ::finished-activities
  (fn [db]
    (let [scenes (get-in db [:course-data :scene-list])
          activities (lessons-activity/flatten-activities (get-in db [:course-data :levels]))]
      (->> activities
           (filter #(lessons-activity/finished? db %))
           (map #(merge % (scene-name->scene (:activity %) scenes)))
           (map #(assoc % :completed true))))))

(defn next-activity
  [db]
  (let [scenes (get-in db [:course-data :scene-list])
          next (get-in db [:progress-data :next])]
      (merge next (scene-name->scene (:activity next) scenes))))

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
           (map #(merge % (scene-name->scene (:activity %) scenes)))
           (map #(if (lessons-activity/finished? db %) (assoc % :completed true) %))
           (filter is-assessment?)))))

(re-frame/reg-sub
  ::progress-loading
  (fn [db]
    (or
      (get-in db [:loading :load-course])
      (get-in db [:loading :load-progress]))))

(defn last-level-done [progress-data]
  (apply max (map #(int (name %)) (keys progress-data))))

(defn last-lesson-done [progress-data level]
    (apply max (map #(int (name %))  (keys ((keyword (str level)) progress-data))))
  )

(re-frame/reg-sub
  ::finished-level-lesson-activities
  (fn [db]
    (let [progress-data (get-in db [:progress-data :finished])
          last-level (last-level-done progress-data)
          last-lesson (last-lesson-done progress-data last-level)
          finished-activities ((keyword (str last-lesson))  ((keyword (str last-level)) progress-data))
          ]
      finished-activities)))

(defn lesson-progress
  [db]
  (let [progress-data (get-in db [:progress-data :finished])
        last-level (last-level-done progress-data)
        last-lesson (last-lesson-done progress-data last-level)

        finished-activities ((keyword (str last-lesson)) ((keyword (str last-level)) progress-data))
        activities (lessons-activity/get-activities db last-level last-lesson)]
    (/ (count finished-activities)
       (count activities))))

(re-frame/reg-sub
  ::lesson-progress
  lesson-progress)

(re-frame/reg-sub
  ::overall-progress
  (fn []
    [(re-frame/subscribe [::course-activities])
     (re-frame/subscribe [::finished-activities])])
  (fn [[course-activities finished-activities]]
    (/ (count finished-activities)
       (count course-activities))))
