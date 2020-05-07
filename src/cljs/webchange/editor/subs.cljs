(ns webchange.editor.subs
  (:require
    [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::current-main-content
  (fn [db]
    (get-in db [:editor :current-main-content])))

(re-frame/reg-sub
  ::transform
  (fn [db]
    (get-in db [:editor :transform])))

(re-frame/reg-sub
  ::selected-object
  (fn [db]
    (get-in db [:editor :selected-object])))

(re-frame/reg-sub
  ::selected-object-action
  (fn [db]
    (get-in db [:editor :selected-object-action])))

(re-frame/reg-sub
  ::selected-object-state
  (fn [db]
    (get-in db [:editor :selected-object-state])))

(re-frame/reg-sub
  ::selected-scene-action
  (fn [db]
    (if-let [{:keys [scene-id action path] :as selected-scene} (get-in db [:editor :selected-scene-action])]
      (let [action-data (get-in db [:scenes scene-id :actions (keyword action)] {})
            path-data (get-in action-data path {})]
        (assoc selected-scene :data path-data)))))

(re-frame/reg-sub
  ::selected-asset
  (fn [db]
    (get-in db [:editor :selected-asset])))

(re-frame/reg-sub
  ::shown-scene-action
  (fn [db]
    (get-in db [:editor :shown-scene-action])))

(re-frame/reg-sub
  ::shown-form
  (fn [db]
    (get-in db [:editor :shown-form])))

(re-frame/reg-sub
  ::course-versions
  (fn [db]
    (get-in db [:editor :course-versions])))

(re-frame/reg-sub
  ::scene-versions
  (fn [db]
    (get-in db [:editor :scene-versions])))

(defn course-datasets
  [db]
  (get-in db [:editor :course-datasets]))

(re-frame/reg-sub
  ::course-datasets
  course-datasets)

(re-frame/reg-sub
  ::current-dataset-id
  (fn [db]
    (get-in db [:editor :current-dataset-id])))

(re-frame/reg-sub
  ::current-dataset-items
  (fn [db]
    (get-in db [:editor :current-dataset-items])))

(re-frame/reg-sub
  ::current-dataset-item-id
  (fn [db]
    (get-in db [:editor :current-dataset-item-id])))

(re-frame/reg-sub
  ::current-dataset-lessons
  (fn [db]
    (get-in db [:editor :current-dataset-lessons])))

(re-frame/reg-sub
  ::current-dataset-lesson-id
  (fn [db]
    (get-in db [:editor :current-dataset-lesson-id])))

(re-frame/reg-sub
  ::dataset
  (fn [db [_ id]]
    (->> (get-in db [:editor :course-datasets])
         (filter #(= id (:id %)))
         first)))

(re-frame/reg-sub
  ::dataset-item
  (fn [db [_ id]]
    (->> (get-in db [:editor :current-dataset-items])
         (filter #(= id (:id %)))
         first)))

(re-frame/reg-sub
  ::dataset-lesson
  (fn [db [_ id]]
    (->> (get-in db [:editor :current-dataset-lessons])
         (filter #(= id (:id %)))
         first)))

(re-frame/reg-sub
  ::new-object-defaults
  (fn [db]
    (get-in db [:editor :new-object-defaults])))