(ns webchange.ui-framework.components.utils
  (:require
    [clojure.string :refer [join]]))

(defn focus
  [el]
  (when (fn? (.-focus el))
    (.focus el)))

(defn get-class-name
  [class-names]
  (->> class-names
       (filter (fn [[_ condition]] condition))
       (map first)
       (distinct)
       (join " ")))

(defn get-bounding-rect
  [el]
  (let [bounding-rect (.getBoundingClientRect el)]
    {:x      (.-x bounding-rect)
     :y      (.-y bounding-rect)
     :width  (.-width bounding-rect)
     :height (.-height bounding-rect)}))
