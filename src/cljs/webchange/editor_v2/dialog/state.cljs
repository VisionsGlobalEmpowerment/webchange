(ns webchange.editor-v2.dialog.state)

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:dialog])
       (vec)))
