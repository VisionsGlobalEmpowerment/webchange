(ns webchange.book-creator.state)

(defn path-to-db
  [relative-path]
  (concat [:book-creator] relative-path))
