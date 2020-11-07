(ns webchange.common.svg-path.path-splitter
  (:require
    [clojure.string :refer [last-index-of trim lower-case upper-case split join]]
    [clojure.edn :as edn]))

(def delimiters (reduce #(concat %1 [%2 (lower-case %2)]) [] ["M" "L" "H" "V" "C" "S" "Q" "T" "A"]))

(defn- parse-path-str
  [path]
  (let [[first & other] (filter (fn [el] (not (contains? #{"" " " "," nil} el))) (split path #"(\s|,|[a-zA-Z])|(?=-)"))]
    (concat [first] (map edn/read-string other))))

(defn- relative->absolute
  [{:keys [x y]} [path-type & coordinates]]
  (concat [(upper-case path-type)]
          (case path-type
            ("m" "l" "t" "c" "q" "s")
            (->> coordinates
                 (partition 2)
                 (map (fn [[dx dy]] [(+ x dx) (+ y dy)]))
                 (flatten))
            ("h")
            (let [[dx] coordinates]
              [(+ x dx)])
            ("v")
            (let [[dy] coordinates]
              [(+ y dy)])
            ("a")
            (let [[rx ry x-axis-rotation large-arc-flag sweep-flag current-x current-y] coordinates]
              [rx ry x-axis-rotation large-arc-flag sweep-flag (+ x current-x) (+ y current-y)])
            coordinates)))

(defn apply-path-to-point
  [{:keys [x y]} [path-type & coordinates]]
  (case path-type
    ("M" "L" "T" "C" "Q" "S" "A")
    (let [[cx cy] (take-last 2 coordinates)]
      {:x cx
       :y cy})
    ("H")
    (let [[x1] coordinates]
      {:x x1
       :y y})
    ("V")
    (let [[y1] coordinates]
      {:x x
       :y y1})
    (throw (js/Error. (str "Unknown path type: " path-type)))))

(defn- relatives->absolutes
  [paths]
  (->> paths
       (reduce
         (fn [[last-point result] rel-path]
           (let [abs-path (relative->absolute last-point rel-path)
                 new-point (apply-path-to-point last-point abs-path)]
             [new-point (conj result abs-path)]))
         [{:x 0 :y 0} []])
       (last)))

(defn- split-path-array
  [delimiters path-array]
  (loop [[current-path & other-path :as path-array] path-array]
    (let [split-index (reduce #(max %1 (last-index-of current-path %2)) -1 delimiters)]
      (if-not (nil? split-index)
        (let [split-str (split-at split-index current-path)
              split-path (map #(->> % (apply str) (trim)) split-str)]
          (recur (concat split-path other-path)))
        (filter (complement empty?) path-array)))))

(defn split-path
  "Split SVG path into separate commands"
  [path]
  (->> [path]
       (split-path-array delimiters)
       (map parse-path-str)
       (relatives->absolutes)))

(defn split-path-str-with
  "Divide SVG path by provided commands"
  [path delimiters]
  (let [absolute-path (->> path split-path flatten (join " "))]
    (split-path-array delimiters [absolute-path])))