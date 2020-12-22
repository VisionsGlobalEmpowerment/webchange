(ns webchange.test.progress.activity
  (:require [clojure.test :refer :all]
            [webchange.sample-data :as sample-data]
            [webchange.progress.activity :as activity]
            [webchange.progress.tags :as tags]
            ))

(deftest next-not-finished-for-test
  (testing "Getting next not finished activity"
    (testing "skip :only tags if student does not have them"
      (is (= {:activity-name "swings", :level 0, :lesson 0, :activity 2}
             (activity/next-not-finished-for [] sample-data/levels {} {:activity-name "home", :level 0, :lesson 0, :activity 0}))))
    (testing "return activity with :only tag if tag is provided"
      (is (= {:activity-name "see-saw", :level 0, :lesson 0, :activity 1}
             (activity/next-not-finished-for [tags/beginner] sample-data/levels {} {:activity-name "home", :level 0, :lesson 0, :activity 0}))))))

