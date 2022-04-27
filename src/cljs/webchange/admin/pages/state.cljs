(ns webchange.admin.pages.state
  (:require
    [webchange.admin.state :as parent-state]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:pages])
       (parent-state/path-to-db)))
