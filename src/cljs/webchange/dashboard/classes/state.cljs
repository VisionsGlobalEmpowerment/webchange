(ns webchange.dashboard.classes.state
  (:require
    [webchange.dashboard.state :as parent-state]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:classes-page])
       (parent-state/path-to-db)))
