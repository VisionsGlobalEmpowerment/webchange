(ns webchange.editor-v2.translator.translator-form.subs
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.db :refer [path-to-db]]
    [webchange.editor-v2.translator.translator-form.utils :refer [get-current-action-data]]))

(defn selected-action
  [db]
  (get-in db (path-to-db [:selected-action])))

(re-frame/reg-sub
  ::selected-action
  selected-action)

(re-frame/reg-sub
  ::current-action-data
  (fn []
    [(re-frame/subscribe [::selected-action])
     (re-frame/subscribe [::current-concept])
     (re-frame/subscribe [::edited-actions-data])])
  (fn [[selected-action-node current-concept data-store]]
    (get-current-action-data selected-action-node current-concept data-store)))

(defn edited-data
  [db]
  (get-in db (path-to-db [:edited-data]) {}))

(re-frame/reg-sub
  ::edited-data
  edited-data)

(defn edited-actions-data
  [db]
  (:actions (edited-data db)))

(re-frame/reg-sub
  ::edited-actions-data
  edited-actions-data)

(defn current-concept
  [db]
  (get-in db (path-to-db [:current-concept])))

(re-frame/reg-sub
  ::current-concept
  current-concept)

