(ns webchange.dashboard.state)

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:dashboard])
       (vec)))
