(ns webchange.test.progress.activity
  (:require [clojure.test :refer :all]
            [webchange.sample-data :as sample-data]
            [webchange.progress.activity :as activity]
            [webchange.progress.tags :as tags]
            ))


(defn- flatten-activity
  [level-idx lesson-idx activity-idx activity]
  (let [activity-name (:activity activity)]
    (assoc activity :level level-idx :lesson lesson-idx :activity activity-idx :activity-name activity-name)))

(defn- flatten-lesson
  [level-idx lesson-idx lesson]
  (map-indexed (fn [activity-idx activity] (flatten-activity level-idx lesson-idx activity-idx activity)) (:activities lesson)))

(defn- flatten-level
  [level-idx level]
  (map-indexed (fn [lesson-idx lesson] (flatten-lesson level-idx lesson-idx lesson)) (:lessons level)))

(defn flatten-activities
  [levels]
  (->> levels
       (map-indexed flatten-level)
       flatten))

(deftest next-not-finished-for-test
  (testing "Getting next not finished activity"
    (testing "skip :only tags if student does not have them"
      (is (= {:activity-name "swings", :level 0, :lesson 0, :activity 2 :unique-id 2}
             (activity/next-not-finished-for [] sample-data/levels {}
                                             {:activity-name "home", :level 0, :lesson 0, :activity 0}))))
    (testing "return activity with :only tag if tag is provided"
      (is (= {:activity-name "see-saw", :level 0, :lesson 0, :activity 1 :unique-id 1}
             (activity/next-not-finished-for [tags/beginner] sample-data/levels {}
                                             {:activity-name "home", :level 0, :lesson 0, :activity 0}))))))
