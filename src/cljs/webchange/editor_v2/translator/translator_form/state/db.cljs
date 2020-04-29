(ns webchange.editor-v2.translator.translator-form.state.db
  (:require
    [webchange.editor-v2.translator.state.db :as parent-db]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:translator-form])
       (parent-db/path-to-db)))