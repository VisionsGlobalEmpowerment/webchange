(ns webchange.editor-v2.question.question-form.state.db
  (:require
    [webchange.editor-v2.question.state.db :as parent-db]))

(def db-key :question-form)

(defn path-to-parent-db
  ([]
   (path-to-parent-db []))
  ([relative-path]
   (parent-db/path-to-db relative-path)))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [db-key])
       (path-to-parent-db)))
