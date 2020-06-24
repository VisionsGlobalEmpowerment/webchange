(ns webchange.student-dashboard.toolbar.sync.state.db)

(defn path-to-db
  [relative-path]
  (concat [:sync-resources-widget] relative-path))
