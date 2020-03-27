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
  ::finished-activities
  (fn [db]
    (let [scenes (get-in db [:course-data :scene-list])
          activities (lessons-activity/flatten-activities (get-in db [:course-data :levels]))]
      (->> activities
           ;(filter #(lessons-activity/finished? db %))
           (map #(merge % (scene-name->scene (:activity %) scenes)))
           (map #(assoc % :completed true))))))

(re-frame/reg-sub
  ::next-activity
  (fn [db]
    (let [scenes (get-in db [:course-data :scene-list])
          next (get-in db [:progress-data :next])]
      (merge next (scene-name->scene (:activity next) scenes)))))

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