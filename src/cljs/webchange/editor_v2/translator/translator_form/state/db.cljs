(ns webchange.editor-v2.translator.translator-form.state.db
  (:require
    [webchange.editor-v2.translator.state.db :as parent-db]))

(def db-key :translator-form)

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
