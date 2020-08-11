(ns webchange.interpreter.subs
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.lessons.subs :as lessons]))

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
  ::current-lesson
  (fn [db]
    (let [{:keys [level lesson]} (get-in db [:progress-data :next])] ;; ToDo get level and lesson from other place
      (-> db
          (lessons/get-level level)
          (lessons/get-lesson lesson)))))

(re-frame/reg-sub
  ::current-lesson-sets
  (fn []
    [(re-frame/subscribe [::current-lesson])])
  (fn [[current-lesson]]
    (->> (:lesson-sets current-lesson)
         (map second))))

(re-frame/reg-sub
  ::lesson-sets-data
  (fn [db [_ lesson-sets-ids]]
    (let [lesson-sets (get-in db [:lessons])]
      (map #(get lesson-sets %) lesson-sets-ids))))

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
