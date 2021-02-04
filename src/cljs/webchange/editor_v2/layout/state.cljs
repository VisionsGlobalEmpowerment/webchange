(ns webchange.editor-v2.layout.state)

(defn path-to-db
  [relative-path]
  (concat [:editor-v2 :scene-data] relative-path))