(ns webchange.editor.form-elements.integer-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [clojure.test.check.generators :as gen]
    [utils.test-generator :refer [check-test-generator-result
                                  test-number-string-generator]]
    [webchange.editor.form-elements.integer :refer [parser]]))

(def attempts-number 100)

(deftest form-input-integer-test
  (testing "parser"
    (testing "should return value for integers"
      (check-test-generator-result
        (test-number-string-generator parser
                                      (gen/tuple
                                        (gen/elements ["" "-" "+"])
                                        gen/nat)
                                      attempts-number)))
    (testing "should return nil for numbers with fractional part"
      (check-test-generator-result
        (test-number-string-generator parser
                                      (gen/tuple
                                        (gen/elements ["" "-" "+"])
                                        gen/nat
                                        (gen/elements ["."])
                                        gen/nat)
                                      attempts-number) :reverse))
    (testing "should return nil for numbers with trailing comma"
      (check-test-generator-result
        (test-number-string-generator parser
                                      (gen/tuple
                                        (gen/elements ["" "-" "+"])
                                        gen/nat
                                        (gen/elements ["."]))
                                      attempts-number) :reverse))
    (testing "should return nil for numbers with heading comma"
      (check-test-generator-result
        (test-number-string-generator parser
                                      (gen/tuple
                                        (gen/elements ["" "-" "+"])
                                        (gen/elements ["."])
                                        gen/nat)
                                      attempts-number) :reverse))
    (testing "should return nil for not-number string"
      (check-test-generator-result
        (test-number-string-generator parser
                                      gen/string
                                      attempts-number) :reverse))
    ))
