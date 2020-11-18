(ns webchange.editor-v2.translator.state.db
  (:require
    [webchange.editor-v2.state :as editor]))

(defn path-to-db
  [relative-path]
  (->> (concat [:translator] relative-path)
       (editor/path-to-db)))
