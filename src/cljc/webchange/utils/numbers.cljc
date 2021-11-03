(ns webchange.utils.numbers)

(defn try-parse-int
  [str]
  "Parse string to integer if string contains only digits. Return string otherwise."
  (if (re-matches #"\d+" str)
    #?(:clj  (.parseInt Integer str)
       :cljs (.parseInt js/Number str))
    str))
