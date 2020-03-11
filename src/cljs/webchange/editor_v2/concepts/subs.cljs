(ns webchange.editor-v2.concepts.subs
  (:require
    [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::datasets
  (fn [db]
    (get-in db [:editor :course-datasets])))

(re-frame/reg-sub
  ::dataset
  (fn [db [_ id]]
    (->> (get-in db [:editor :course-datasets])
         (filter #(= id (:id %)))
         first)))

(re-frame/reg-sub
  ::dataset-items
  (fn [db]
    (get-in db [:editor :course-dataset-items])))

(defn get-item
  [db id]
  (->> (get-in db [:editor :course-dataset-items])
       (filter #(= id (:id %)))
       first))

(re-frame/reg-sub
  ::dataset-item
  (fn [db [_ id]]
    (get-item db id)))

(re-frame/reg-sub
  ::delete-dataset-item-modal-state
  (fn [db]
    (get-in db [:editor-v2 :concepts :delete-dataset-item-modal-state])))
