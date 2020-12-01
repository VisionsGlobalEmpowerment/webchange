(ns webchange.interpreter.subs
  (:require
    [re-frame.core :as re-frame]
    [webchange.progress.activity :as common.activity]
    [webchange.interpreter.lessons.activity :as activity]))

(defn course-datasets
  [db]
  (get-in db [:datasets]))

(re-frame/reg-sub
  ::course-datasets
  course-datasets)

(re-frame/reg-sub
  ::course-dataset
  (fn []
    [(re-frame/subscribe [::course-datasets])])
  (fn [[datasets] [_ dataset-id]]
    (some (fn [{:keys [id] :as dataset}] (and (= id dataset-id) dataset)) datasets)))

(re-frame/reg-sub
  ::course-scenes
  (fn [db]
    (get-in db [:course-data :scene-list])))

(re-frame/reg-sub
  ::after-current-activity
  (fn [db]
    (->> (get-in db [:progress-data :next])
         (activity/next-not-finished-for db))))

(re-frame/reg-sub
  ::lesson-sets-data
  (fn [db [_ lesson-sets-ids]]
    (let [lesson-sets (get-in db [:lessons])]
      (map #(get lesson-sets %) lesson-sets-ids))))

(defn- dissoc-keys
  [data keys-to-remove]
  (reduce (fn [data key-to-remove]
            (dissoc data key-to-remove))
          data
          keys-to-remove))

(re-frame/reg-sub
  ::lessons-data
  (fn [db [_ {:keys [exclude-items-fields]}]]
    (let [lessons (get-in db [:lessons])
          dataset-items (get-in db [:dataset-items])]
      (->> lessons
           (map (fn [[lesson-name lesson-data]]
                  (let [lesson-items (->> (:item-ids lesson-data)
                                          (map (fn [item-id] (cond-> (get dataset-items item-id)
                                                                     (some? exclude-items-fields) (dissoc-keys exclude-items-fields)))))]
                    [lesson-name (assoc lesson-data :items lesson-items)])))
           (into {})))))

(re-frame/reg-sub
  ::current-lesson-sets-data
  (fn [db]
    (let [activity-name (:current-scene db)
          {:keys [level lesson]} (activity/name->activity-action db activity-name)
          lesson-sets (-> (get-in db [:course-data :levels])
                          (common.activity/get-level level)
                          :lessons
                          (common.activity/get-lesson lesson)
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
