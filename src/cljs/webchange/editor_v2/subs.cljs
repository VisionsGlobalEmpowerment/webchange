(ns webchange.editor-v2.subs
  (:require
    [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::diagram-mode
  (fn [db]
    (get-in db [:editor-v2 :diagram-mode])))

(re-frame/reg-sub
  ::current-action
  (fn [db]
    (get-in db [:editor-v2 :current-action])))

(re-frame/reg-sub
  ::course-concept
  (fn [db]
    (->> (get-in db [:editor :course-datasets])
         (some (fn [dataset]
                 (and (= "concepts" (:name dataset))
                      dataset))))))

(re-frame/reg-sub
  ::course-dataset-items
  (fn [db]
    (get-in db [:dataset-items])))

(re-frame/reg-sub
  ::course-info
  (fn [db]
    (get-in db [:editor :course-info])))
