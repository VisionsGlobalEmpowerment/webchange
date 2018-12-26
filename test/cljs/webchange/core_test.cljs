(ns webchange.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [webchange.core :as core]))

(deftest fake-test-pass
  (testing "should pass"
    (is (= 2 2))))
