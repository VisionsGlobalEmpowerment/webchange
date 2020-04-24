(ns webchange.editor-v2.translator.translator-form.db)

(defn path-to-db
  [relative-path]
  (concat [:editor-v2 :translator-form] relative-path))
