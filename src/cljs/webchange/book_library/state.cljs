(ns webchange.book-library.state
  (:require
    [re-frame.core :as re-frame]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:book-library])
       (vec)))
