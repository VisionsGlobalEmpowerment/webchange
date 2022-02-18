(ns utils.compare-maps
  (:require
    [clojure.data :refer [diff]]))

(defn- print-data
  ([data max-depth]
   (print-data data 1 max-depth))
  ([data indent max-depth]
   (when (< indent max-depth)
     (let [indent-str (apply str (repeat (* indent 2) " "))]
       (cond
         (map? data) (doseq [[field-name field-data] data]
                       (print indent-str field-name)
                       (print-data field-data (inc indent) max-depth))
         (sequential? data) (if (empty? data)
                              (print indent-str data)
                              (doseq [[field-name field-data] (->> (map-indexed vector data)
                                                                   (filter (fn [[_ value]] (some? value))))]
                                (print indent-str (str field-name ":"))
                                (print-data field-data (inc indent) (max (+ indent 2) max-depth))))
         :else (print indent-str data))))))

(defn print-maps-comparison
  ([actual expected]
   (print-maps-comparison actual expected {}))
  ([actual expected {:keys [name max-depth] :or {max-depth 5}}]
   (println "print maps comparison:" name)
   (let [dif (diff actual expected)
         actual-diff (first dif)
         expected-diff (second dif)]
     (println "actual:")
     (print-data actual-diff max-depth)
     (println "expected:")
     (print-data expected-diff max-depth))))
