(ns webchange.common.svg-path.path-to-transitions
  (:require
    [clojure.string :as s]
    ["svg-arc-to-cubic-bezier" :as arcToBezier]
    [webchange.common.svg-path.path-splitter :refer [split-path apply-path-to-point]]
    [webchange.common.svg-path.path-element :refer [length]]
    [webchange.interpreter.renderer.scene.components.svg-path :refer [get-svg-path]]
    [webchange.utils.numbers :refer [to-precision]]))

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
  [{:keys [x y]} prev-path [path-type & coordinates] precision]
  (let [->precision (partial (fn [precision coordinates]
                               (-> coordinates
                                   (update :x to-precision precision)
                                   (update :y to-precision precision)))
                             precision)]
    (case path-type
      ("M" "L")
      (let [[x1 y1] coordinates]
        [(->precision {:x x1 :y y1})])
      ("H")
      (let [[x1] coordinates]
        [(->precision {:x x1 :y y})])
      ("V")
      (let [[y1] coordinates]
        [(->precision {:x x :y y1})])
      ("C")
      (let [[x1 y1 x2 y2 x3 y3] coordinates]
        [{:motionPath {:type "cubic"
                       :path [(->precision {:x x :y y})
                              (->precision {:x x1 :y y1})
                              (->precision {:x x2 :y y2})
                              (->precision {:x x3 :y y3})]}}])
      ("S")
      (let [[x2 y2 x3 y3] coordinates
            prev-path-type (first prev-path)]
        (if (contains? #{"C" "S"} prev-path-type)
          (let [[x01 y01 x02 y02] (take-last 4 prev-path)
                x1 (- (* 2 x02) x01)
                y1 (- (* 2 y02) y01)]
            [{:motionPath {:type "cubic"
                           :path [(->precision {:x x :y y})
                                  (->precision {:x x1 :y y1})
                                  (->precision {:x x2 :y y2})
                                  (->precision {:x x3 :y y3})]}}])
          [{:motionPath {:type "cubic"
                         :path [(->precision {:x x :y y})
                                (->precision {:x x2 :y y2})
                                (->precision {:x x2 :y y2})
                                (->precision {:x x3 :y y3})]}}]))
      ("Q")
      (let [[x1 y1 x2 y2] coordinates]
        [{:motionPath [(->precision {:x x :y y})
                       (->precision {:x x1 :y y1})
                       (->precision {:x x2 :y y2})]}])
      ("T")
      (let [[x2 y2] coordinates
            prev-path-type (first prev-path)]
        (if (contains? #{"Q"} prev-path-type)
          (let [[x01 y01 x02 y02] (take-last 4 prev-path)
                x1 (- (* 2 x02) x01)
                y1 (- (* 2 y02) y01)]
            [{:motionPath [(->precision {:x x :y y})
                           (->precision {:x x1 :y y1})
                           (->precision {:x x2 :y y2})]}])
          [(->precision {:x x2 :y y2})]))
      ("A")
      (let [[rx ry x-axis-rotation large-arc-flag sweep-flag current-x current-y] coordinates
            params (clj->js {:px            x
                             :py            y
                             :cx            current-x
                             :cy            current-y
                             :rx            rx
                             :ry            ry
                             :xAxisRotation x-axis-rotation
                             :largeArcFlag  large-arc-flag
                             :sweepFlag     sweep-flag})
            curves (->> params (arcToBezier) (js->clj))]
        (map
         (fn [{x1 "x1" y1 "y1" x2 "x2" y2 "y2" x3 "x" y3 "y"}]
           {:motionPath [(->precision {:x x1 :y y1})
                         (->precision {:x x2 :y y2})
                         (->precision {:x x3 :y y3})]})
         curves))
      (throw (js/Error. (str "Unknown path type: " path-type))))))

(defn- get-transitions
  ([paths options]
   (get-transitions paths options nil))
  ([paths options precision]
   (->> paths
        (reduce
         (fn [[last-point prev-path result] path]
           (let [transitions (get-transition last-point prev-path path precision)
                 new-point (apply-path-to-point last-point path)]
             [new-point path (concat result (map #(merge % options) transitions))]))
         [{:x 0 :y 0} nil []])
        (last))))

(defn- transition->path
  [transition]
  (let [thru-bezier? (and (some? (:motionPath transition))
                          (empty? (-> transition :motionPath :type)))
        c-curve? (or (= "cubic" (-> transition :motionPath :type))
                     (and thru-bezier? (= 3 (-> transition :motionPath count))))
        q-curve? (or (= "quadratic" (-> transition :motionPath :type))
                     (and thru-bezier? (= 2 (-> transition :motionPath count))))
        
        flat-curve #(let [bezier-values (if (-> % :motionPath :path)
                                          (-> % :motionPath :path rest)
                                          (:motionPath %))]
                      (flatten (map (fn [{:keys [x y]}] [x y]) bezier-values)))]
    (cond
      c-curve? (concat ["C"] (flat-curve transition))
      q-curve? (concat ["Q"] (flat-curve transition))
      :else ["L" (:x transition) (:y transition)])))

(defn- get-transitions-durations
  [transitions total-duration origin precision]
  (let [lengths (->> transitions
                     (reduce
                      (fn [[{:keys [x y] :as last-point} result] transition]
                        (let [transition-path (transition->path transition)
                              new-point (apply-path-to-point last-point transition-path)
                              path (concat ["M" x y] transition-path)
                              calculated-length (length (s/join " " path))]
                          [new-point (conj result calculated-length)]))
                      [origin []])
                     (last))
        total-length (reduce + 0 lengths)]
    (map #(-> (/ % total-length)
              (* total-duration)
              (to-precision precision))
         lengths)))

(defn path->transitions
  ([params]
   (path->transitions params nil))
  ([{:keys [path letter-path origin duration scale]} {:keys [precision] :or {precision 6}}]
   (let [absolute-paths (->> (if (some? letter-path) (get-svg-path letter-path {:trace? true}) path)
                             (split-path)
                             (map (partial apply-scale scale))
                             (map (partial apply-origin origin)))
         transitions (get-transitions absolute-paths {} precision)
         transition-durations (get-transitions-durations transitions duration origin precision)]
     (map merge transitions (map #(hash-map :duration %) transition-durations)))))

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
