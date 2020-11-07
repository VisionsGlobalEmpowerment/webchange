(ns webchange.sw-utils.state.db)

(defn path-to-db
  [relative-path]
  (concat [:service-worker] relative-path))

