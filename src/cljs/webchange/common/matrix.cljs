(ns webchange.common.matrix)

(defn matrix-objects-list
  [object scene-id draw-object]
  (let [{:keys [el width height dx dy max skip]} object
        skip-coordinates (partition 2 skip)]
    (->> (for [y (range (/ height dy)) x (range (/ width dx))] [x y])
         (filter (fn [coordinate] (not (some #(= coordinate %) skip-coordinates))))
         (take max)
         (map (fn [[x y]]
                ^{:key (str scene-id x y)}
                [draw-object scene-id el {:x (* x dx)
                                          :y (* y dy)}])))))
