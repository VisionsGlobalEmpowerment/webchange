(ns webchange.editor-v2.translator.translator-form.audio-assets.add-audio.state.db
  (:require
    [webchange.editor-v2.translator.translator-form.state.db :as parent-db]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:add-audio-panel])
       (parent-db/path-to-db)))
