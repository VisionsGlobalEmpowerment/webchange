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

(defn get-error-messages
  [spec data]
  (let [explain-data (s/explain-data spec data)
        errors-data (or (:clojure.spec.alpha/problems explain-data) (:cljs.spec.alpha/problems explain-data))]
    (cond
      (map? data) (reduce (fn [result problem]
                            (assoc result
                              (get-problem-field problem)
                              (phrase {} problem)))
                          {}
                          errors-data)
      :else (map (fn [problem]
                   (phrase {} problem))
                 errors-data))))

(defn validate
  [spec data]
  (let [errors (get-error-messages spec data)]
    (if-not (empty? errors)
      errors
      nil)))
