(ns webchange.editor-v2.state)

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:editor-v2])
       (vec)))
