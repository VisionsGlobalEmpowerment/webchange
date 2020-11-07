(ns utils.compare-maps
  (:require
    [clojure.data :refer [diff]]))

(defn print-maps-comparison
  ([actual expected]
   (print-maps-comparison actual expected ""))
  ([actual expected name]
    (println "--- print maps comparison ---" name)
    (let [dif (diff actual expected)
          actual-diff (first dif)
          expected-diff (second dif)]
      (println "actual:")
      (println actual-diff)
      (println "expected:")
      (println expected-diff))))
