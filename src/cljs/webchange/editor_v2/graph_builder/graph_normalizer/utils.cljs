(ns webchange.editor-v2.graph-builder.graph-normalizer.utils)

(defn get-copy-name
  [origin-name number]
  (-> origin-name
      (name)
      (str "-copy-" number)
      (keyword)))
