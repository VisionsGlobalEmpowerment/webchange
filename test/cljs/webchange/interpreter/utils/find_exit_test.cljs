(ns webchange.interpreter.utils.find-exit-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [webchange.interpreter.utils.find-exit :refer [find-exit-position]]
    [webchange.interpreter.utils.scenes-list :as scenes-list]))

(deftest test-find-exit-position--stay-at-scene
  (let [from "home"
        to "home"]
    (let [actual-result (find-exit-position from to scenes-list/data)
          expected-result nil]
      (is (= actual-result expected-result)))))

(deftest test-find-exit-position--find-target
  (let [from "map"
        to "home"]
    (let [actual-result (find-exit-position from to scenes-list/data)
          expected-result {:name "home", :x 881, :y 490, :object "home"}]
      (is (= actual-result expected-result)))))

(deftest test-find-exit-position--find-target-container
  (let [from "map"
        to "see-saw"]
    (let [actual-result (find-exit-position from to scenes-list/data)
          expected-result {:name "park", :x 1447, :y 860, :object "park"}]
      (is (= actual-result expected-result)))))

(deftest test-find-exit-position--exit-with-scene-object
  (let [from "home"
        to "see-saw"]
    (let [actual-result (find-exit-position from to scenes-list/data)
          expected-result {:name "map", :x 1457, :y 630, :object "door"}]
      (is (= actual-result expected-result)))))

(deftest test-find-exit-position--exit-with-back-button
  (let [from "see-saw"
        to "swings"]
    (let [actual-result (find-exit-position from to scenes-list/data)
          expected-result {:name "park", :x 100, :y 100, :object "back"}]
      (is (= actual-result expected-result)))))
