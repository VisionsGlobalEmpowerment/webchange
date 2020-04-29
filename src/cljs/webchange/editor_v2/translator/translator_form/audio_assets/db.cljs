(ns webchange.editor-v2.translator.translator-form.audio-assets.db
  (:require
    [webchange.editor-v2.translator.translator-form.db :as parent-db]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:audio-assets])
       (parent-db/path-to-db)))
