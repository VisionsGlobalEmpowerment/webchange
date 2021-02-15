(ns webchange.editor-v2.layout.flipbook.state
  (:require
    [webchange.editor-v2.layout.state :as db]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:stage-text-control])
       (db/path-to-db)))
