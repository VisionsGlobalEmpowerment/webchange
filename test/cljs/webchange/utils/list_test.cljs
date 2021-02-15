(ns webchange.utils.list-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [webchange.utils.list :as utils]))

(deftest test-without-item
  (testing "remove present item"
    (let [list ["a" "b" "c" "d" "e"]
          item "b"]
      (let [actual-result (utils/without-item list item)
            expected-result ["a" "c" "d" "e"]]
        (is (= actual-result expected-result)))))
  (testing "remove absent item"
    (let [list ["a" "b" "c" "d" "e"]
          item "x"]
      (let [actual-result (utils/without-item list item)
            expected-result ["a" "b" "c" "d" "e"]]
        (is (= actual-result expected-result))))))

(deftest test-insert-at-position
  (testing "insert at middle position"
    (let [list ["a" "b" "c" "d" "e"]
          item "x"
          position 3]
      (let [actual-result (utils/insert-at-position list item position)
            expected-result ["a" "b" "c" "x" "d" "e"]]
        (is (= actual-result expected-result)))))
  (testing "insert at first position"
    (let [list ["a" "b" "c" "d" "e"]
          item "x"
          position 0]
      (let [actual-result (utils/insert-at-position list item position)
            expected-result ["x" "a" "b" "c" "d" "e"]]
        (is (= actual-result expected-result)))))
  (testing "insert at last position"
    (let [list ["a" "b" "c" "d" "e"]
          item "x"
          position 5]
      (let [actual-result (utils/insert-at-position list item position)
            expected-result ["a" "b" "c" "d" "e" "x"]]
        (is (= actual-result expected-result)))))
  (testing "insert at pre-last position"
    (let [list ["a" "b" "c" "d" "e"]
          item "x"
          position 4]
      (let [actual-result (utils/insert-at-position list item position)
            expected-result ["a" "b" "c" "d" "x" "e"]]
        (is (= actual-result expected-result))))))

(deftest test-remove-at-position
  (testing "remove from middle position"
    (let [list ["a" "b" "c" "d" "e"]
          position 3]
      (let [actual-result (utils/remove-at-position list position)
            expected-result ["a" "b" "c" "e"]]
        (is (= actual-result expected-result)))))
  (testing "remove from first position"
    (let [list ["a" "b" "c" "d" "e"]
          position 0]
      (let [actual-result (utils/remove-at-position list position)
            expected-result ["b" "c" "d" "e"]]
        (is (= actual-result expected-result)))))
  (testing "remove from last position"
    (let [list ["a" "b" "c" "d" "e"]
          position 4]
      (let [actual-result (utils/remove-at-position list position)
            expected-result ["a" "b" "c" "d"]]
        (is (= actual-result expected-result))))))

(deftest test-move-item
  (testing "move to middle position"
    (let [list ["a" "b" "c" "d" "e"]
          position-from 1
          position-to 3]
      (let [actual-result (utils/move-item list position-from position-to)
            expected-result ["a" "c" "d" "b" "e"]]
        (is (= actual-result expected-result)))))
  (testing "move to first position"
    (let [list ["a" "b" "c" "d" "e"]
          position-from 1
          position-to 0]
      (let [actual-result (utils/move-item list position-from position-to)
            expected-result ["b" "a" "c" "d" "e"]]
        (is (= actual-result expected-result)))))
  (testing "move to last position"
    (let [list ["a" "b" "c" "d" "e"]
          position-from 2
          position-to 4]
      (let [actual-result (utils/move-item list position-from position-to)
            expected-result ["a" "b" "d" "e" "c"]]
        (is (= actual-result expected-result)))))
  (testing "move to same position"
    (let [list ["a" "b" "c" "d" "e"]
          position-from 1
          position-to 1]
      (let [actual-result (utils/move-item list position-from position-to)
            expected-result ["a" "b" "c" "d" "e"]]
        (is (= actual-result expected-result))))))
