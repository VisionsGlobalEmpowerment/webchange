(ns utils.test-generator
  (:require
    [cljs.test :refer [deftest testing is]]
    [clojure.test.check :as tc]
    [clojure.test.check.properties :as prop :include-macros true]))

(defn test-number-string-generator
  [f generator attempts]
  (tc/quick-check
    attempts
    (prop/for-all
      [sample generator]
      (f (clojure.string/join "" sample)))))

(defn check-test-generator-result
  [test-result & [reverse-result]]
  (let [passed? (if (= reverse-result :reverse)
                  (not (:result test-result))
                  (:result test-result))]
    (is passed?)
    (when (not passed?) (println "Failed for tuple:" (:fail test-result)))))