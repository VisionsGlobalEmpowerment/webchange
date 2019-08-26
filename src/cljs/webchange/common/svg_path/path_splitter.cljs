(ns webchange.common.svg-path.path-splitter
  (:require
    [clojure.string :refer [last-index-of trim]]))

(defn split-path
  "Divide SVG path on uninterrupted curves or lines"
  ([path]
   (split-path path ["M" "m"]))
  ([path delimiters]
   (let [split-path-array (fn [path-array delimiters]
                            (loop [[current-path & other-path :as path-array] path-array]
                              (let [split-index (reduce #(max %1 (last-index-of current-path %2)) -1 delimiters)]
                                (if-not (nil? split-index)
                                  (let [split-str (split-at split-index current-path)
                                        split-path (map #(->> % (apply str) (trim)) split-str)]
                                    (recur (concat split-path other-path)))
                                  (filter (complement empty?) path-array)))))]
     (split-path-array [path] delimiters))))
