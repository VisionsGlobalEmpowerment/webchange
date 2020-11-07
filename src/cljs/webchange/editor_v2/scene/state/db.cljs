(ns webchange.editor-v2.scene.state.db)

(defn path-to-db
  [relative-path]
  (concat [:editor-v2 :scene-data] relative-path))
