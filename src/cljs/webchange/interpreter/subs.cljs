(ns webchange.interpreter.subs
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.lessons.activity :as activity]))

(re-frame/reg-sub
  ::course-datasets
  (fn [db]
    (get-in db [:datasets])))

(re-frame/reg-sub
  ::course-dataset
  (fn []
    [(re-frame/subscribe [::course-datasets])])
  (fn [[datasets] [_ dataset-id]]
    (some (fn [{:keys [id] :as dataset}] (and (= id dataset-id) dataset)) datasets)))

(re-frame/reg-sub
  ::after-current-activity
  (fn [db]
    (let [scenes (get-in db [:course-data :scene-list])
          next (->> (get-in db [:progress-data :next])
                    (activity/next-not-finished-for db))]
      (->> (:activity next)
           (keyword)
           (get scenes)))))

(re-frame/reg-sub
  ::current-lesson-sets-data
  (fn [db]
    (let [activity-name (:current-scene db)
          {:keys [level lesson]} (activity/name->activity-action db activity-name)
          lesson-sets (-> (get-in db [:course-data :levels])
                          (activity/get-level level)
                          :lessons
                          (activity/get-lesson lesson)
                          :lesson-sets)
          lessons (get-in db [:lessons])
          loaded-lessons (get-in db [:sandbox :loaded-lessons])
          get-lesson (fn [[name id]] (or (get loaded-lessons name)
                                         (get lessons id)))]
      (map get-lesson lesson-sets))))

(re-frame/reg-sub
  ::dataset-items
  (fn [db]
    (get-in db [:dataset-items])))

(re-frame/reg-sub
  ::dataset-item
  (fn []
    [(re-frame/subscribe [::dataset-items])])
  (fn [[dataset-items] [_ dataset-item-id]]
    (get dataset-items dataset-item-id)))
