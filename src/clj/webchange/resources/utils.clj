(ns webchange.resources.utils
  (:require
    [clojure.string :as s]))

(defn- get-values-list
  [data]
  (cond
    (sequential? data) (flatten (map get-values-list data))
    (map? data) (get-values-list (vals data))
    :else data))

(defn find-resources
  [data]
  (->> (get-values-list data)
       (filter #(and (string? %)
                     (s/starts-with? % "/raw/")))
       (distinct)))