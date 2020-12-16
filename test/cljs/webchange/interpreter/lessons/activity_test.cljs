(ns webchange.interpreter.lessons.activity-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [webchange.interpreter.lessons.activity :as activity]
    [webchange.sample-data :as sample-data]))

(deftest flatten-activities
  (let [flattened (activity/flatten-activities sample-data/levels)]
    (testing "should contain level lesson activity"
      (is (some? (-> flattened first :level)))
      (is (some? (-> flattened first :lesson)))
      (is (some? (-> flattened first :activity))))))
