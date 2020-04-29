(ns webchange.editor-v2.translator.translator-form.db
  (:require
    [webchange.editor-v2.translator.db :as parent-db]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:translator-form])
       (parent-db/path-to-db)))