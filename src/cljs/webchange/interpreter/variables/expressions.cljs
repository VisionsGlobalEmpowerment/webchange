(ns webchange.interpreter.variables.expressions
  (:require
    [clojure.string :refer [starts-with?]]
    [webchange.interpreter.variables.core :as core]))

(defn- string-keyword?
  [expression]
  (and (string? expression)
       (starts-with? expression ":")))

(declare eval-expression)

(defn- do-eval-expression
  [db action [ex & args]]
  (let [right-expression (map #(eval-expression db action %) args)]
    (case ex
      "eq" (apply = right-expression)
      "and" (every? identity right-expression)
      "or" (some identity right-expression)
      "len" (-> right-expression first count)
      "." (apply get right-expression))))

(defn- expression->value
  [db action expression]
  (let [variable? #(-> % first (= "@"))
        param? #(-> % first (= "#"))
        ->name #(subs % 1)]
    (cond
      (variable? expression) (core/get-variable (->name expression))
      (param? expression) (get-in action [:params (keyword (->name expression))])
      :else expression)))

(defn- expression->keyword
  [expression]
  (-> expression (subs 1) (keyword)))

(defn eval-expression
  [db action expression]
  (cond
    (string-keyword? expression) (expression->keyword expression)
    (string? expression) (expression->value db action expression)
    (int? expression) expression
    (keyword? expression) expression
    (vector? expression) (do-eval-expression db action expression)))
