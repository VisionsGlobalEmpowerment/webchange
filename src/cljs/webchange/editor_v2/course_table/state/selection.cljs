(ns webchange.editor-v2.course-table.state.selection
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.course-table.state.db :as db]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:selection])
       (db/path-to-db)))

(defn selection
  [db]
  (get-in db (path-to-db [])))

(re-frame/reg-sub ::selection selection)

(re-frame/reg-event-fx
  ::set-selection
  (fn [{:keys [db]} [_ type data]]
    {:db (assoc-in db (path-to-db []) {:type type
                                       :data data})}))
