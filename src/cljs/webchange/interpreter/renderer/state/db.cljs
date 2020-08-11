(ns webchange.interpreter.renderer.state.db)

(defn path-to-db
  [relative-path]
  (concat [:interpreter :scene] relative-path))
