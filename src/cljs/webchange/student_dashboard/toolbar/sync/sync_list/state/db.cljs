(ns webchange.student-dashboard.toolbar.sync.sync-list.state.db)

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:sync-resources-widget])))
