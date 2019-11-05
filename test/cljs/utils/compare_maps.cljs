(ns utils.compare-maps
  (:require
    [clojure.data :refer [diff]]))

(defn compare-maps
  [actual expected]
  (reduce
    (fn [res key]
      (assoc res key (= (get actual key) (get expected key))))
    {}
    (keys actual)))

(defn get-type
  [value]
  (cond
    (map? value) :map
    :else :unhandled))

(defn print-maps-comparison
  ([actual expected]
   (println "--- print maps comparison ---")
   (print-maps-comparison actual expected []))
  ([actual expected path]
   (doseq [key (keys actual)]
     (let [actual-value (get actual key)
           expected-value (get expected key)
           equal? (= actual-value expected-value)
           new-path (conj path key)]
       (if-not equal?
         (case (get-type actual-value)
           :map (print-maps-comparison actual-value expected-value new-path)
           (do (println (clojure.string/join " -> " new-path))
               (println "  " actual-value " != " expected-value))))))))
