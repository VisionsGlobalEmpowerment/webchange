(ns webchange.utils.numbers)

(defn integer-str?
  [str]
  (re-matches #"\d+" str))

(defn number-str?
  [str]
  (re-matches #"\d+(.\d+)?" str))

(defn try-parse-int
  [str]
  "Parse string to integer if string contains only digits. Return string otherwise."
  (if (and (string? str)
           (integer-str? str))
    #?(:clj  (.parseInt Integer str)
       :cljs (.parseInt js/Number str))
    str))

(defn try-parse-number
  ([str]
   (try-parse-number str false))
  ([str force?]
   "Parse string to integer if string contains only digits. Return string otherwise."
   (if (or force?
           (and (string? str)
                (integer-str? str)))
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
