(ns utils.compare-maps)

(defn compare-maps
  [actual expected]
  (reduce
    (fn [res key]
      (assoc res key (= (get actual key) (get expected key))))
    {}
    (keys actual)))