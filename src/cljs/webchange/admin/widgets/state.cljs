(ns webchange.admin.widgets.state
  (:require
    [webchange.admin.state :as parent-state]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:widgets])
       (parent-state/path-to-db)))
