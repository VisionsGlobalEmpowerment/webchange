(ns webchange.parent-dashboard.state)

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:parent-dashboard])
       (vec)))
