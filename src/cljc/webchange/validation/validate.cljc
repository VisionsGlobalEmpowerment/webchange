(ns webchange.validation.validate
  (:require
    [clojure.spec.alpha :as s]
    [webchange.validation.specs.student]
    [webchange.validation.phrases]
    [phrase.alpha :refer [phrase]]))

(defn get-problem-field
  [problem]
  (let [path (:path problem)
        predicate (:pred problem)
        get-field-keyword #(when (sequential? %) (->> % (last) (last)))]
    (or (first path) (get-field-keyword predicate))))

(defn phrase-problems
  ([problems]
   (phrase-problems problems true))
  ([problems with-fields?]
   (if with-fields?
     (reduce (fn [result problem]
               (assoc result
                 (get-problem-field problem)
                 (phrase {} problem)))
             {}
             problems)
     (map (fn [problem]
            (phrase {} problem))
          problems))))

(defn get-error-messages
  [spec data]
  (let [explain-data (s/explain-data spec data)
        errors-data (or (:clojure.spec.alpha/problems explain-data) (:cljs.spec.alpha/problems explain-data))]
    (phrase-problems errors-data (map? data))))

(defn validate
  [spec data]
  (let [errors (get-error-messages spec data)]
    (if-not (empty? errors)
      errors
      nil)))
