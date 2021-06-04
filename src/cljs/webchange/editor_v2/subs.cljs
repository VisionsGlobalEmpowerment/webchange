(ns webchange.editor-v2.subs
  (:require
    [re-frame.core :as re-frame]))

(defn course-concept
  [db]
  (->> (get-in db [:editor :course-datasets])
       (some (fn [dataset]
               (and (= "concepts" (:name dataset))
                    dataset)))))

(re-frame/reg-sub
  ::course-concept
  course-concept)

(defn course-datasets
  [db]
  (get-in db [:editor :course-datasets]))

(re-frame/reg-sub
  ::course-datasets
  course-datasets)

(defn course-dataset-items
  [db]
  (get-in db [:dataset-items]))

(defn dataset-concept
  [db]
  (->> (course-datasets db)
       first))

(re-frame/reg-sub
  ::course-dataset-items
  course-dataset-items)

(re-frame/reg-sub
  ::course-info
  (fn [db]
    (get-in db [:editor :course-info])))

(re-frame/reg-sub
  ::course-has-concepts?
  (fn [db]
    (get-in db [:editor :course-has-concepts?] false)))

