(ns webchange.ui.utils.get-class-name
  (:require
    [clojure.string :refer [join]]))

(defn get-class-name
  [class-names]
  (->> class-names
       (filter (fn [[_ condition]] condition))
       (map first)
       (distinct)
       (join " ")))
