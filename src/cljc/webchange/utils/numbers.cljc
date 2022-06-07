(ns webchange.utils.numbers)

(defn number-str?
  [str]
  (re-matches #"\d+" str))

(defn try-parse-int
  [str]
  "Parse string to integer if string contains only digits. Return string otherwise."
  (if (and (string? str)
           (number-str? str))
    #?(:clj  (.parseInt Integer str)
       :cljs (.parseInt js/Number str))
    str))

(defn to-precision
  [number precision]
  (if (number? precision)
    (let [k (Math/pow 10 precision)]
      (-> (* number k)
          (Math/floor)
          (/ k)))
    number))
