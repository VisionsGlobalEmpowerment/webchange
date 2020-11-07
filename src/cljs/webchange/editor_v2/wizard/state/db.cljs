(ns webchange.editor-v2.wizard.state.db)

(defn path-to-db
  [relative-path]
  (concat [:editor-v2 :wizard] relative-path))
