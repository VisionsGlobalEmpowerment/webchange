(ns webchange.progress.utils)

(defn idx->keyword
  [idx]
  (cond
    (keyword? idx) idx
    (number? idx) (-> idx str keyword)))
