(ns webchange.ui-framework.components.utils
  (:require
    [clojure.string :refer [join]]))

(defn get-class-name
  [class-names]
  (->> class-names
       (filter (fn [[_ condition]] condition))
       (map first)
       (join " ")))
