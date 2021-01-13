(ns webchange.editor-v2.question.state.db
  (:require
    [webchange.editor-v2.state :as editor]))

(defn path-to-db
  [relative-path]
  (->> (concat [:question] relative-path)
       (editor/path-to-db)))
