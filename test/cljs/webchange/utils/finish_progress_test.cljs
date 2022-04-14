(ns webchange.utils.finish-progress-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.progress.finish :refer [activity-finished? course-finished? get-finished-progress]]))

(declare course-data)

(deftest test-get-finished-progress
  (testing "Should return finished progress for not specified data"
    (let [actual-result (get-finished-progress course-data)
          expected-result (sorted-map :0 (sorted-map :0 [0 1 2 3]
                                                     :1 [4 5 6]
                                                     :2 [7 8 9 10])
                                      :1 (sorted-map :0 [59 60 61 62]
                                                     :1 [63 64])
                                      :2 (sorted-map :0 [111 112 113 114]
                                                     :1 [115]
                                                     :2 [116 117 118]
                                                     :3 [119]))]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result))))

  (testing "Should return finished progress for specified data"
    (let [actual-result (get-finished-progress course-data {:level-idx    1
                                                            :lesson-idx   0
                                                            :activity-idx 2})
          expected-result (sorted-map :0 (sorted-map :0 [0 1 2 3]
                                                     :1 [4 5 6]
                                                     :2 [7 8 9 10])
                                      :1 (sorted-map :0 [59 60 61]))]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-activity-finished?
  (testing "Should return correct value"
    (let [current-progress (sorted-map :0 (sorted-map :0 [0 1 2 3]
                                                      :1 [4 5 6]
                                                      :2 [7 8 9 10])
                                       :1 (sorted-map :0 [59 60 61]))]
      (is (= (activity-finished? current-progress {:level-idx    0
                                                   :lesson-idx   1
                                                   :activity-idx 2})
             true))
      (is (= (activity-finished? current-progress {:level-idx    :0
                                                   :lesson-idx   :1
                                                   :activity-idx 2})
             true))
      (is (= (activity-finished? current-progress {:level-idx   0
                                                   :lesson-idx  1
                                                   :activity-id 6})
             true))
      (is (= (activity-finished? current-progress {:level-idx    0
                                                   :lesson-idx   1
                                                   :activity-idx 6})
             false))
      (is (= (activity-finished? current-progress {:level-idx    2
                                                   :lesson-idx   1
                                                   :activity-idx 1})
             false))
      (is (= (activity-finished? current-progress {:level-idx    0
                                                   :lesson-idx   3
                                                   :activity-idx 1})
             false)))))

(deftest test-course-finished?
  (testing "should return true when all activities are finished"
    (let [current-progress (sorted-map :0 (sorted-map :0 [0 1 2 3]
                                                      :1 [4 5 6]
                                                      :2 [7 8 9 10])
                                       :1 (sorted-map :0 [59 60 61 62]
                                                      :1 [63 64])
                                       :2 (sorted-map :0 [111 112 113 114]
                                                      :1 [115]
                                                      :2 [116 117 118]
                                                      :3 [119]))]
      (is (= (course-finished? course-data current-progress) true))))

  (testing "should return false when the last activity is not finished"
    (let [current-progress (sorted-map :0 (sorted-map :0 [0 1 2 3]
                                                      :1 [4 5 6]
                                                      :2 [7 8 9 10])
                                       :1 (sorted-map :0 [59 60 61 62]
                                                      :1 [63 64])
                                       :2 (sorted-map :0 [111 112 113 114]
                                                      :1 [115]
                                                      :2 [116 117 118]
                                                      :3 []))]
      (is (= (course-finished? course-data current-progress) false))))

  (testing "should return false when the last lesson is not in progress"
    (let [current-progress (sorted-map :0 (sorted-map :0 [0 1 2 3]
                                                      :1 [4 5 6]
                                                      :2 [7 8 9 10])
                                       :1 (sorted-map :0 [59 60 61 62]
                                                      :1 [63 64])
                                       :2 (sorted-map :0 [111 112 113 114]
                                                      :1 [115]
                                                      :2 [116 117 118]))]
      (is (= (course-finished? course-data current-progress) false))))

  (testing "should return true when not last activity is not finished"
    (let [current-progress (sorted-map :0 (sorted-map :0 [0 1 2 3]
                                                      :1 [4 5 6]
                                                      :2 [7 8 9 10])
                                       :1 (sorted-map :0 [59 60 61 62]
                                                      :1 [63 64])
                                       :2 (sorted-map :0 [111 112 113]
                                                      :1 [115]
                                                      :2 [116 117 118]
                                                      :3 [119]))]
      (is (= (course-finished? course-data current-progress) true)))))

(def course-data {:levels [{:name    "Level 1"
                            :level   1
                            :lessons [{:name       "Lesson 1"
                                       :type       "lesson"
                                       :lesson     1
                                       :activities [{:activity "cinema-new" :unique-id 0}
                                                    {:activity "running-with-letters" :unique-id 1 :time-expected 300}
                                                    {:only nil :activity "letter-intro-1" :unique-id 2}
                                                    {:activity "first-words-book-letter-a" :unique-id 3}]}
                                      {:name       "Lesson 2"
                                       :type       "lesson"
                                       :lesson     2
                                       :activities [{:activity "interactive-read-aloud-newest" :unique-id 4 :time-expected 300}
                                                    {:activity "writing-1" :unique-id 5 :time-expected 300}
                                                    {:activity "i-spy-1" :unique-id 6 :time-expected 300}]}
                                      {:name       "Lesson 3"
                                       :type       "lesson"
                                       :lesson     3
                                       :activities [{:only          nil
                                                     :activity      "cinema-new"
                                                     :unique-id     7
                                                     :tags-by-score {:new-tag-0 [0 100]}
                                                     :time-expected 300}
                                                    {:only nil :activity "running-with-letters" :unique-id 8 :time-expected 300}
                                                    {:activity "letter-intro-1" :unique-id 9}
                                                    {:activity "first-words-book-letter-e" :unique-id 10 :time-expected 300}]}]}
                           {:name    "Level 2"
                            :level   2
                            :lessons [{:name       "Lesson 12"
                                       :type       "lesson"
                                       :lesson     1
                                       :activities [{:activity "cinema-new" :unique-id 59 :time-expected 300}
                                                    {:activity "running-with-letters" :unique-id 60}
                                                    {:activity "letter-intro-1" :unique-id 61 :time-expected 300}
                                                    {:scored        true
                                                     :activity      "first-words-book-letter-p"
                                                     :unique-id     62
                                                     :time-expected 300}]}
                                      {:name       "Lesson 13"
                                       :type       "lesson"
                                       :lesson     2
                                       :activities [{:activity      "interactive-read-aloud-4-sizwes-smile"
                                                     :unique-id     63
                                                     :time-expected 300}
                                                    {:activity "rhyming-at-and-ig" :unique-id 64 :time-expected 300}]}]}
                           {:lessons [{:activities [{:activity "cinema-new" :unique-id 111 :time-expected 300}
                                                    {:activity "running-with-letters" :unique-id 112 :time-expected 300}
                                                    {:activity "letter-intro-1" :unique-id 113 :time-expected 300}
                                                    {:scored        true
                                                     :activity      "first-words-book-letter-g"
                                                     :unique-id     114
                                                     :time-expected 300}]}
                                      {:activities [{:activity      "interactive-read-aloud-sizwes-smile-2"
                                                     :unique-id     115
                                                     :time-expected 300}]}
                                      {:activities [{:activity "cinema-new" :unique-id 116 :time-expected 300}
                                                    {:activity "running-with-letters" :unique-id 117 :time-expected 300}
                                                    {:activity "letter-intro-1" :unique-id 118 :time-expected 300}]}
                                      {:activities [{:activity "sandbox-body-parts-1" :unique-id 119}]}]}]})
