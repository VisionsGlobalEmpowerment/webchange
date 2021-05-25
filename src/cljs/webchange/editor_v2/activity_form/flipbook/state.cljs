(ns webchange.editor-v2.activity-form.flipbook.state)

(defn path-to-db
  [relative-path]
  (concat [:book-creator] relative-path))
