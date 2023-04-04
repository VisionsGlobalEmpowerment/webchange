(ns webchange.utils.numbers)

(defn integer-str?
  [str]
  (re-matches #"\d+" str))

(defn number-str?
  [str]
  (re-matches #"\d+(.\d+)?" str))

(defn try-parse-int
  "Parse string to integer if string contains only digits. Return string otherwise."
  [str]
  (if (and (string? str)
           (integer-str? str))
    #?(:clj  (.parseInt Integer str)
       :cljs (.parseInt js/Number str))
    str))

(defn try-parse-number
  "Parse string to integer if string contains only digits. Return string otherwise."
  ([str]
   (try-parse-number str false))
  ([str force?]
   (if (or force?
           (and (string? str)
                (number-str? str)))
     #?(:clj  (.parseFloat Float str)
        :cljs (.parseFloat js/Number str))
     str)))

(defn to-precision
  [number precision]
  (if (number? precision)
    (let [k (Math/pow 10 precision)]
      (-> (* number k)
          (Math/floor)
          (/ k)))
    number))
