(ns webchange.admin.widgets.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.state :as parent-state]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:widgets])
       (parent-state/path-to-db)))

(re-frame/reg-fx
  ::callback
  (fn [[callback & params]]
    (when (fn? callback)
      (apply callback params))))