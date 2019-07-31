(ns webchange.editor.common.actions.subs
  (:require
    [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::form-data
  (fn [db]
    (let [action-data (get-in db [:editor :action-form :data])
          path (get-in db [:editor :action-form :path])]
      (get-in action-data path {}))))

(re-frame/reg-sub
  ::form-data-hash
  (fn [db]
    (-> db
        (get-in [:editor :action-form])
        hash)))

(re-frame/reg-sub
  ::form-data-original
  (fn [db]
    (get-in db [:editor :action-form :data])))