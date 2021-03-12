(ns webchange.common.svg-path.path-to-transitions
  (:require
    [clojure.edn :as edn]
    [clojure.string :as s]
    [webchange.common.svg-path.path-splitter :refer [split-path apply-path-to-point]]
    [webchange.common.svg-path.path-element :refer [length]]
    [svg-arc-to-cubic-bezier :as arcToBezier]))

(defn- apply-origin
  [{:keys [x y]} [path-type & coordinates]]
  (case path-type
    ("M" "L" "T" "Q" "S" "C")
    (->> coordinates
         (partition 2)
         (map (fn [[fx fy]] [(+ x fx) (+ y fy)]))
         (flatten)
         (concat [path-type]))
    ("H")
    (let [[x1] coordinates]
      [path-type (+ x x1)])
    ("V")
    (let [[y1] coordinates]
      [path-type (+ y y1)])
    ("A")
    (let [[rx ry x-axis-rotation large-arc-flag sweep-flag current-x current-y] coordinates]
      [path-type rx ry x-axis-rotation large-arc-flag sweep-flag (+ x current-x) (+ y current-y)])
    (throw (js/Error. (str "Unknown path type: " path-type)))))

(defn- apply-scale
  [scale [path-type & coordinates :as path]]
  (if-not (nil? scale)
    (let [scale-x (:x scale)
          scale-y (:y scale)]
      (case path-type
        ("M" "L" "T" "Q" "S" "C")
        (->> coordinates
             (partition 2)
             (map (fn [[fx fy]] [(* scale-x fx) (* scale-y fy)]))
             (flatten)
             (concat [path-type]))
        ("H")
        (let [[x1] coordinates]
          [path-type (* scale-x x1)])
        ("V")
        (let [[y1] coordinates]
          [path-type (* scale-y y1)])
        ("A")
        (let [[rx ry x-axis-rotation large-arc-flag sweep-flag current-x current-y] coordinates]
          [path-type (* scale-x rx) (* scale-y ry) x-axis-rotation large-arc-flag sweep-flag (* scale-x current-x) (* scale-y current-y)])
        (throw (js/Error. (str "Unknown path type: " path-type)))))
    path))

(defn- get-transition
  [{:keys [x y]} prev-path [path-type & coordinates]]
  (case path-type
    ("M" "L")
    (let [[x1 y1] coordinates]
      [{:x x1
        :y y1}] )
    ("H")
    (let [[x1] coordinates]
      [{:x x1
        :y y}])
    ("V")
    (let [[y1] coordinates]
      [{:x x
        :y y1}])
    ("C")
    (let [[x1 y1 x2 y2 x3 y3] coordinates]
      [{:bezier [{:x x1 :y y1} {:x x2 :y y2} {:x x3 :y y3}]}])
    ("S")
    (let [[x2 y2 x3 y3] coordinates
          prev-path-type (first prev-path)]
      (if (contains? #{"C" "S"} prev-path-type)
        (let [[x01 y01 x02 y02] (take-last 4 prev-path)
              x1 (- (* 2 x02) x01)
              y1 (- (* 2 y02) y01)]
          [{:bezier [{:x x1 :y y1} {:x x2 :y y2} {:x x3 :y y3}]}])
        [{:bezier [{:x x2 :y y2} {:x x2 :y y2} {:x x3 :y y3}]}]))
    ("Q")
    (let [[x1 y1 x2 y2] coordinates]
      [{:bezier [{:x x1 :y y1} {:x x2 :y y2}]}])
    ("T")
    (let [[x2 y2] coordinates
          prev-path-type (first prev-path)]
      (if (contains? #{"Q"} prev-path-type)
        (let [[x01 y01 x02 y02] (take-last 4 prev-path)
              x1 (- (* 2 x02) x01)
              y1 (- (* 2 y02) y01)]
          [{:bezier [{:x x1 :y y1} {:x x2 :y y2}]}])
        [{:x x2 :y y2}]))
    ("A")
    (let [[rx ry x-axis-rotation large-arc-flag sweep-flag current-x current-y] coordinates
          params (clj->js {:px x
                           :py y
                           :cx current-x
                           :cy current-y
                           :rx rx
                           :ry ry
                           :xAxisRotation x-axis-rotation
                           :largeArcFlag large-arc-flag
                           :sweepFlag sweep-flag})
          curves (->> params ((.-default arcToBezier)) (js->clj))]
      (map
        (fn [{x1 "x1" y1 "y1" x2 "x2" y2 "y2" x3 "x" y3 "y"}]
          {:bezier [{:x x1 :y y1} {:x x2 :y y2} {:x x3 :y y3}]})
        curves))
    (throw (js/Error. (str "Unknown path type: " path-type)))))

(defn- get-transitions
  [options paths]
  (->> paths
       (reduce
         (fn [[last-point prev-path result] path]
           (let [transitions (get-transition last-point prev-path path)
                 new-point (apply-path-to-point last-point path)]
             [new-point path (concat result (map #(merge % options) transitions))]))
         [{:x 0 :y 0} nil []])
       (last)))

(defn- transition->path
  [transition]
  (let [c-curve? #(= 3 (count (:bezier %)))
        q-curve? #(= 2 (count (:bezier %)))
        flat-curve #(flatten (map (fn [{:keys [x y]}] [x y]) (:bezier %)))]
    (cond
      (c-curve? transition) (concat ["C"] (flat-curve transition))
      (q-curve? transition) (concat ["Q"] (flat-curve transition))
      :else ["L" (:x transition) (:y transition)])))

(defn- get-transitions-durations
  [transitions total-duration origin]
  (let [lengths (->> transitions
                     (reduce
                       (fn [[{:keys [x y] :as last-point} result] transition]
                         (let [transition-path (transition->path transition)
                               new-point (apply-path-to-point last-point transition-path)
                               path (concat ["M" x y ] transition-path)
                               length (length (s/join " " path))]
                           [new-point (conj result length)]))
                       [origin []])
                     (last))
        total-length (reduce + 0 lengths)]
    (map #(* total-duration (/ % total-length)) lengths)))

(defn path->transitions
  [{:keys [path origin duration scale]}]
  (let [absolute-paths (->> (split-path path)
                            (map (partial apply-scale scale))
                            (map (partial apply-origin origin)))
        transitions (->> absolute-paths (get-transitions {}))
        transition-durations (get-transitions-durations transitions duration origin)]
    (map merge transitions (map #(hash-map :duration %) transition-durations))))

(defn- points-distance
  [p1 p2]
  (Math/sqrt (+ (Math/pow (- (:x p1) (:x p2)) 2) (Math/pow (- (:y p1) (:y p2)) 2))))

(defn get-moves-lengths
  [path-str]
  (let [lengths (->> (split-path path-str)
                     (reduce
                       (fn [[last-point result] path]
                         (let [new-point (apply-path-to-point last-point path)]
                           [new-point
                            (if (= "M" (first path))
                              (conj result (points-distance last-point new-point))
                              result)]))
                       [{:x 0 :y 0} []])
                     (last))]
    lengths))
