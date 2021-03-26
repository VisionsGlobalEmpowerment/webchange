(ns webchange.common.svg-path.path-element)

(defn length
  "Get SVG path length in local coordinates."
  [path]
  (let [path-element (.createElementNS js/document "http://www.w3.org/2000/svg" "path")]
    (.setAttribute path-element "d" path)
    (.getTotalLength path-element)))

(defn ->points
  "Transform SVG path to points with given precision"
  [path precision]
  (let [path-element (doto (.createElementNS js/document "http://www.w3.org/2000/svg" "path")
                       (.setAttribute "d" path))
        length (.getTotalLength path-element)]
    (->> (range 0 length precision)
         (map #(.getPointAtLength path-element %))
         (map (fn [point] {:x (.-x point) :y (.-y point)})))))
