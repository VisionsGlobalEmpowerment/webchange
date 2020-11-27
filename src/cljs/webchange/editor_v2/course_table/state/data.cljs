(ns webchange.editor-v2.course-table.state.data
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.course-table.state.data-utils :refer [prepare-course-data]]
    [webchange.editor-v2.course-table.state.db :as db]
    [webchange.editor-v2.events :as editor-events]
    [webchange.interpreter.events :as interpreter]
    [webchange.subs :as subs]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:data] relative-path)
       (db/path-to-db)))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_ course-id]]
    {:dispatch [::interpreter/load-course-data course-id]}))

(re-frame/reg-sub
  ::table-data
  (fn []
    [(re-frame/subscribe [::subs/course-data])
     (re-frame/subscribe [::subs/scenes-data [:skills]])])
  (fn [[course-data scenes-data]]
    (prepare-course-data course-data scenes-data)))
