(ns webchange.editor-v2.course-table.state.db
  (:require
    [webchange.editor-v2.state :as db]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:course-table] relative-path)
       (db/path-to-db)))
