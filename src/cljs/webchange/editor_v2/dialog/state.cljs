(ns webchange.editor-v2.dialog.state
  (:require
    [re-frame.core :as re-frame]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:dialog])
       (vec)))
