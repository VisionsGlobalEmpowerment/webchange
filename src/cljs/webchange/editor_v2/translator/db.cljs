(ns webchange.editor-v2.translator.db)

(defn path-to-db
  [relative-path]
  (concat [:editor-v2 :translator] relative-path))
