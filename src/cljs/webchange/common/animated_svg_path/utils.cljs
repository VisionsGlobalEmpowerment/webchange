(ns webchange.common.animated-svg-path.utils
  (:require
    [clojure.string :refer [last-index-of trim]]))

(defn get-params
  [current-params default-params]
  (reduce (fn [result param-key]
            (assoc result param-key
                          (get current-params param-key (get default-params param-key))))
          {}
          (keys default-params)))

(defn path-length
  "Get SVG path length in local coordinates."
  [path]
  (let [path-element (.createElementNS js/document "http://www.w3.org/2000/svg" "path")]
    (.setAttribute path-element "d" path)
    (.getTotalLength path-element)))

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
                                  path-array))))]
     (split-path-array [path] delimiters))))
