(ns webchange.utils.course-data-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.utils.course-data :as c]))

(defonce course-data
         {:levels [{:tmp-id  "lvl-1"
                    :lessons [{:tmp-id     "lsn-1-1"
                               :activities [{:tmp-id "act-1-1-1" :activity "activity-111" :unique-id 111}
                                            {:tmp-id "act-1-1-2" :activity "activity-112" :unique-id 112}]}
                              {:tmp-id     "lsn-1-2"
                               :activities [{:tmp-id "act-1-2-1" :activity "activity-121" :unique-id 121}
                                            {:tmp-id "act-1-2-2" :activity "activity-122" :unique-id 122}
                                            {:tmp-id "act-1-2-3" :activity "activity-123" :unique-id 123}]}
                              {:tmp-id     "lsn-1-3"
                               :activities [{:tmp-id "act-1-3-1" :activity "activity-131" :unique-id 131}
                                            {:tmp-id "act-1-3-2" :activity "activity-132" :unique-id 132}]}]}
                   {:tmp-id  "lvl-2"
                    :lessons [{:tmp-id     "lsn-2-1"
                               :activities [{:tmp-id "act-2-1-1" :activity "activity-211" :unique-id 211}
                                            {:tmp-id "act-2-1-2" :activity "activity-212" :unique-id 212}]}
                              {:tmp-id     "lsn-2-2"
                               :activities [{:tmp-id "act-2-2-1" :activity "activity-221" :unique-id 221}
                                            {:tmp-id "act-2-2-2" :activity "activity-222" :unique-id 222}
                                            {:tmp-id "act-2-2-3" :activity "activity-223" :unique-id 223}]}]}
                   {:tmp-id  "lvl-3"
                    :lessons [{:tmp-id     "lsn-3-1"
                               :activities [{:tmp-id "act-3-1-1" :activity "activity-311" :unique-id 311}
                                            {:tmp-id "act-3-1-2" :activity "activity-312" :unique-id 312}
                                            {:tmp-id "act-3-1-3" :activity "activity-313" :unique-id 313}]}
                              {:tmp-id     "lsn-3-2"
                               :activities [{:tmp-id "act-3-2-1" :activity "activity-321" :unique-id 321}
                                            {:tmp-id "act-3-2-2" :activity "activity-322" :unique-id 322}
                                            {:tmp-id "act-3-2-3" :activity "activity-323" :unique-id 323}]}
                              {:tmp-id     "lsn-3-3"
                               :activities [{:tmp-id "act-3-3-1" :activity "activity-331" :unique-id 331}
                                            {:tmp-id "act-3-3-2" :activity "activity-332" :unique-id 332}]}]}]})


(defn- shrink-result
  [course-data depth]
  (case depth
    :lvl (->> (:levels course-data)
              (map :tmp-id)
              (vec))
    :lsn (->> (:levels course-data)
              (map (fn [{:keys [tmp-id lessons]}]
                     [tmp-id (->> lessons
                                  (map :tmp-id)
                                  (vec))]))
              (vec))
    :act (->> (:levels course-data)
              (map (fn [{:keys [tmp-id lessons]}]
                     [tmp-id (->> lessons
                                  (map (fn [{:keys [tmp-id activities]}]
                                         [tmp-id (->> activities
                                                      (map :tmp-id)
                                                      (vec))]))
                                  (vec))]))
              (vec))))

(deftest add-course-level
  (testing "Empty level should be add before the first level"
    (let [actual-result (c/add-level course-data {:target-level 1
                                                  :position     :before}
                                     {:tmp-id "lvl-0"})]
      (is (= (shrink-result actual-result :lvl) ["lvl-0" "lvl-1" "lvl-2" "lvl-3"]))))
  (testing "Empty level should be add after the last level"
    (let [actual-result (c/add-level course-data {:target-level 3
                                                  :position     :after}
                                     {:tmp-id "lvl-0"})]
      (is (= (shrink-result actual-result :lvl) ["lvl-1" "lvl-2" "lvl-3" "lvl-0"])))))

(deftest move-course-level
  (testing "Place the first level before the second"
    (let [actual-result (c/move-level course-data {:source-level 1
                                                   :target-level 2
                                                   :position     :before})]
      (is (= (shrink-result actual-result :lvl) ["lvl-1" "lvl-2" "lvl-3"]))))
  (testing "Place the first level after the second"
    (let [actual-result (c/move-level course-data {:source-level 1
                                                   :target-level 2
                                                   :position     :after})]
      (is (= (shrink-result actual-result :lvl) ["lvl-2" "lvl-1" "lvl-3"]))))
  (testing "Place the first level after the last"
    (let [actual-result (c/move-level course-data {:source-level 1
                                                   :target-level 3
                                                   :position     :after})]
      (is (= (shrink-result actual-result :lvl) ["lvl-2" "lvl-3" "lvl-1"]))))
  (testing "Place the last level before the first"
    (let [actual-result (c/move-level course-data {:source-level 3
                                                   :target-level 1
                                                   :position     :before})]
      (is (= (shrink-result actual-result :lvl) ["lvl-3" "lvl-1" "lvl-2"]))))
  (testing "Place the last level after the first"
    (let [actual-result (c/move-level course-data {:source-level 3
                                                   :target-level 1
                                                   :position     :after})]
      (is (= (shrink-result actual-result :lvl) ["lvl-1" "lvl-3" "lvl-2"]))))
  (testing "Place the last level after the second"
    (let [actual-result (c/move-level course-data {:source-level 3
                                                   :target-level 2
                                                   :position     :after})]
      (is (= (shrink-result actual-result :lvl) ["lvl-1" "lvl-2" "lvl-3"])))))

(deftest remove-course-level
  (testing "Remove the first level"
    (let [actual-result (c/remove-level course-data 1)]
      (is (= (shrink-result actual-result :lvl) ["lvl-2" "lvl-3"]))))
  (testing "Remove the last level"
    (let [actual-result (c/remove-level course-data 3)]
      (is (= (shrink-result actual-result :lvl) ["lvl-1" "lvl-2"])))))

(deftest add-course-lesson
  (testing "Add empty lesson before the first lesson"
    (let [actual-result (c/add-lesson course-data {:target-level  1
                                                   :target-lesson 1
                                                   :position      :before}
                                      {:tmp-id "lsn-0-0"})]
      (is (= (shrink-result actual-result :lsn) [["lvl-1" ["lsn-0-0" "lsn-1-1" "lsn-1-2" "lsn-1-3"]]
                                                 ["lvl-2" ["lsn-2-1" "lsn-2-2"]]
                                                 ["lvl-3" ["lsn-3-1" "lsn-3-2" "lsn-3-3"]]]))))
  (testing "Add empty lesson after the last lesson"
    (let [actual-result (c/add-lesson course-data {:target-level  3
                                                   :target-lesson 3
                                                   :position      :after}
                                      {:tmp-id "lsn-0-0"})]
      (is (= (shrink-result actual-result :lsn) [["lvl-1" ["lsn-1-1" "lsn-1-2" "lsn-1-3"]]
                                                 ["lvl-2" ["lsn-2-1" "lsn-2-2"]]
                                                 ["lvl-3" ["lsn-3-1" "lsn-3-2" "lsn-3-3" "lsn-0-0"]]])))))

(deftest move-course-lesson
  (testing "Move the first lesson before the second"
    (let [actual-result (c/move-lesson course-data {:source-level  1
                                                    :source-lesson 1
                                                    :target-level  1
                                                    :target-lesson 2
                                                    :position      :before})]
      (is (= (shrink-result actual-result :lsn) [["lvl-1" ["lsn-1-1" "lsn-1-2" "lsn-1-3"]]
                                                 ["lvl-2" ["lsn-2-1" "lsn-2-2"]]
                                                 ["lvl-3" ["lsn-3-1" "lsn-3-2" "lsn-3-3"]]]))))
  (testing "Move the first lesson after the second"
    (let [actual-result (c/move-lesson course-data {:source-level  1
                                                    :source-lesson 1
                                                    :target-level  1
                                                    :target-lesson 2
                                                    :position      :after})]
      (is (= (shrink-result actual-result :lsn) [["lvl-1" ["lsn-1-2" "lsn-1-1" "lsn-1-3"]]
                                                 ["lvl-2" ["lsn-2-1" "lsn-2-2"]]
                                                 ["lvl-3" ["lsn-3-1" "lsn-3-2" "lsn-3-3"]]]))))
  (testing "Move the first lesson before the last"
    (let [actual-result (c/move-lesson course-data {:source-level  1
                                                    :source-lesson 1
                                                    :target-level  1
                                                    :target-lesson 3
                                                    :position      :before})]
      (is (= (shrink-result actual-result :lsn) [["lvl-1" ["lsn-1-2" "lsn-1-1" "lsn-1-3"]]
                                                 ["lvl-2" ["lsn-2-1" "lsn-2-2"]]
                                                 ["lvl-3" ["lsn-3-1" "lsn-3-2" "lsn-3-3"]]]))))
  (testing "Move the first lesson after the last"
    (let [actual-result (c/move-lesson course-data {:source-level  1
                                                    :source-lesson 1
                                                    :target-level  1
                                                    :target-lesson 3
                                                    :position      :after})]
      (is (= (shrink-result actual-result :lsn) [["lvl-1" ["lsn-1-2" "lsn-1-3" "lsn-1-1"]]
                                                 ["lvl-2" ["lsn-2-1" "lsn-2-2"]]
                                                 ["lvl-3" ["lsn-3-1" "lsn-3-2" "lsn-3-3"]]]))))

  (testing "Move the last lesson before the first"
    (let [actual-result (c/move-lesson course-data {:source-level  1
                                                    :source-lesson 3
                                                    :target-level  1
                                                    :target-lesson 1
                                                    :position      :before})]
      (is (= (shrink-result actual-result :lsn) [["lvl-1" ["lsn-1-3" "lsn-1-1" "lsn-1-2"]]
                                                 ["lvl-2" ["lsn-2-1" "lsn-2-2"]]
                                                 ["lvl-3" ["lsn-3-1" "lsn-3-2" "lsn-3-3"]]]))))
  (testing "Move the last lesson after the first"
    (let [actual-result (c/move-lesson course-data {:source-level  1
                                                    :source-lesson 3
                                                    :target-level  1
                                                    :target-lesson 1
                                                    :position      :after})]
      (is (= (shrink-result actual-result :lsn) [["lvl-1" ["lsn-1-1" "lsn-1-3" "lsn-1-2"]]
                                                 ["lvl-2" ["lsn-2-1" "lsn-2-2"]]
                                                 ["lvl-3" ["lsn-3-1" "lsn-3-2" "lsn-3-3"]]]))))
  (testing "Move the last lesson before the second from the end"
    (let [actual-result (c/move-lesson course-data {:source-level  1
                                                    :source-lesson 3
                                                    :target-level  1
                                                    :target-lesson 2
                                                    :position      :before})]
      (is (= (shrink-result actual-result :lsn) [["lvl-1" ["lsn-1-1" "lsn-1-3" "lsn-1-2"]]
                                                 ["lvl-2" ["lsn-2-1" "lsn-2-2"]]
                                                 ["lvl-3" ["lsn-3-1" "lsn-3-2" "lsn-3-3"]]]))))
  (testing "Move the last lesson after the second from the end"
    (let [actual-result (c/move-lesson course-data {:source-level  1
                                                    :source-lesson 3
                                                    :target-level  1
                                                    :target-lesson 2
                                                    :position      :after})]
      (is (= (shrink-result actual-result :lsn) [["lvl-1" ["lsn-1-1" "lsn-1-2" "lsn-1-3"]]
                                                 ["lvl-2" ["lsn-2-1" "lsn-2-2"]]
                                                 ["lvl-3" ["lsn-3-1" "lsn-3-2" "lsn-3-3"]]]))))

  (testing "Move the first lesson in the first level after the last lesson in the last level"
    (let [actual-result (c/move-lesson course-data {:source-level  1
                                                    :source-lesson 1
                                                    :target-level  3
                                                    :target-lesson 3
                                                    :position      :after})]
      (is (= (shrink-result actual-result :lsn) [["lvl-1" ["lsn-1-2" "lsn-1-3"]]
                                                 ["lvl-2" ["lsn-2-1" "lsn-2-2"]]
                                                 ["lvl-3" ["lsn-3-1" "lsn-3-2" "lsn-3-3" "lsn-1-1"]]]))))
  (testing "Move the last lesson in the last level before the first lesson in the first level"
    (let [actual-result (c/move-lesson course-data {:source-level  3
                                                    :source-lesson 3
                                                    :target-level  1
                                                    :target-lesson 1
                                                    :position      :before})]
      (is (= (shrink-result actual-result :lsn) [["lvl-1" ["lsn-3-3" "lsn-1-1" "lsn-1-2" "lsn-1-3"]]
                                                 ["lvl-2" ["lsn-2-1" "lsn-2-2"]]
                                                 ["lvl-3" ["lsn-3-1" "lsn-3-2"]]])))))

(deftest remove-course-lesson
  (testing "Remove the first lesson in the first level"
    (let [actual-result (c/remove-lesson course-data 1 1)]
      (is (= (shrink-result actual-result :lsn) [["lvl-1" ["lsn-1-2" "lsn-1-3"]]
                                                 ["lvl-2" ["lsn-2-1" "lsn-2-2"]]
                                                 ["lvl-3" ["lsn-3-1" "lsn-3-2" "lsn-3-3"]]]))))
  (testing "Remove the last lesson in the last level"
    (let [actual-result (c/remove-lesson course-data 3 3)]
      (is (= (shrink-result actual-result :lsn) [["lvl-1" ["lsn-1-1" "lsn-1-2" "lsn-1-3"]]
                                                 ["lvl-2" ["lsn-2-1" "lsn-2-2"]]
                                                 ["lvl-3" ["lsn-3-1" "lsn-3-2"]]])))))

(deftest add-course-activity
  (testing "Add activity before the first activity in the first lesson of the first level"
    (let [actual-result (c/add-activity course-data {:activity-slug   "new-activity"
                                                     :target-level    1
                                                     :target-lesson   1
                                                     :target-activity 1
                                                     :position        :before}
                                        {:tmp-id "act-0-0-0"})]
      (is (= (shrink-result actual-result :act) [["lvl-1" [["lsn-1-1" ["act-0-0-0" "act-1-1-1" "act-1-1-2"]]
                                                           ["lsn-1-2" ["act-1-2-1" "act-1-2-2" "act-1-2-3"]]
                                                           ["lsn-1-3" ["act-1-3-1" "act-1-3-2"]]]]
                                                 ["lvl-2" [["lsn-2-1" ["act-2-1-1" "act-2-1-2"]]
                                                           ["lsn-2-2" ["act-2-2-1" "act-2-2-2" "act-2-2-3"]]]]
                                                 ["lvl-3" [["lsn-3-1" ["act-3-1-1" "act-3-1-2" "act-3-1-3"]]
                                                           ["lsn-3-2" ["act-3-2-1" "act-3-2-2" "act-3-2-3"]]
                                                           ["lsn-3-3" ["act-3-3-1" "act-3-3-2"]]]]]))
      (let [new-activity (get-in actual-result [:levels 0 :lessons 0 :activities 0])]
        (is (= (:activity new-activity) "new-activity"))
        (is (= (:unique-id new-activity) 333)))))
  (testing "Add activity after the last activity in the last lesson of the last level"
    (let [actual-result (c/add-activity course-data {:activity-slug   "new-activity"
                                                     :target-level    3
                                                     :target-lesson   3
                                                     :target-activity 2
                                                     :position        :after}
                                        {:tmp-id "act-0-0-0"})]
      (is (= (shrink-result actual-result :act) [["lvl-1" [["lsn-1-1" ["act-1-1-1" "act-1-1-2"]]
                                                           ["lsn-1-2" ["act-1-2-1" "act-1-2-2" "act-1-2-3"]]
                                                           ["lsn-1-3" ["act-1-3-1" "act-1-3-2"]]]]
                                                 ["lvl-2" [["lsn-2-1" ["act-2-1-1" "act-2-1-2"]]
                                                           ["lsn-2-2" ["act-2-2-1" "act-2-2-2" "act-2-2-3"]]]]
                                                 ["lvl-3" [["lsn-3-1" ["act-3-1-1" "act-3-1-2" "act-3-1-3"]]
                                                           ["lsn-3-2" ["act-3-2-1" "act-3-2-2" "act-3-2-3"]]
                                                           ["lsn-3-3" ["act-3-3-1" "act-3-3-2" "act-0-0-0"]]]]]))
      (let [new-activity (get-in actual-result [:levels 2 :lessons 2 :activities 2])]
        (is (= (:activity new-activity) "new-activity"))
        (is (= (:unique-id new-activity) 333))))))

(deftest remove-course-activity
  (testing "Remove the first activity in the first lesson of the first level"
    (let [actual-result (c/remove-activity course-data 1 1 1)]
      (is (= (shrink-result actual-result :act) [["lvl-1" [["lsn-1-1" ["act-1-1-2"]]
                                                           ["lsn-1-2" ["act-1-2-1" "act-1-2-2" "act-1-2-3"]]
                                                           ["lsn-1-3" ["act-1-3-1" "act-1-3-2"]]]]
                                                 ["lvl-2" [["lsn-2-1" ["act-2-1-1" "act-2-1-2"]]
                                                           ["lsn-2-2" ["act-2-2-1" "act-2-2-2" "act-2-2-3"]]]]
                                                 ["lvl-3" [["lsn-3-1" ["act-3-1-1" "act-3-1-2" "act-3-1-3"]]
                                                           ["lsn-3-2" ["act-3-2-1" "act-3-2-2" "act-3-2-3"]]
                                                           ["lsn-3-3" ["act-3-3-1" "act-3-3-2"]]]]]))))
  (testing "Remove the last activity in the last lesson of the last level"
    (let [actual-result (c/remove-activity course-data 3 3 2)]
      (is (= (shrink-result actual-result :act) [["lvl-1" [["lsn-1-1" ["act-1-1-1" "act-1-1-2"]]
                                                           ["lsn-1-2" ["act-1-2-1" "act-1-2-2" "act-1-2-3"]]
                                                           ["lsn-1-3" ["act-1-3-1" "act-1-3-2"]]]]
                                                 ["lvl-2" [["lsn-2-1" ["act-2-1-1" "act-2-1-2"]]
                                                           ["lsn-2-2" ["act-2-2-1" "act-2-2-2" "act-2-2-3"]]]]
                                                 ["lvl-3" [["lsn-3-1" ["act-3-1-1" "act-3-1-2" "act-3-1-3"]]
                                                           ["lsn-3-2" ["act-3-2-1" "act-3-2-2" "act-3-2-3"]]
                                                           ["lsn-3-3" ["act-3-3-1"]]]]])))))

(deftest move-course-activity
  (testing "Move 1-1-1 activity after the 3-3-2"
    (let [actual-result (c/move-activity course-data {:source-level    1
                                                      :source-lesson   1
                                                      :source-activity 1
                                                      :target-level    3
                                                      :target-lesson   3
                                                      :target-activity 2
                                                      :position        :after})]
      (is (= (shrink-result actual-result :act) [["lvl-1" [["lsn-1-1" ["act-1-1-2"]]
                                                           ["lsn-1-2" ["act-1-2-1" "act-1-2-2" "act-1-2-3"]]
                                                           ["lsn-1-3" ["act-1-3-1" "act-1-3-2"]]]]
                                                 ["lvl-2" [["lsn-2-1" ["act-2-1-1" "act-2-1-2"]]
                                                           ["lsn-2-2" ["act-2-2-1" "act-2-2-2" "act-2-2-3"]]]]
                                                 ["lvl-3" [["lsn-3-1" ["act-3-1-1" "act-3-1-2" "act-3-1-3"]]
                                                           ["lsn-3-2" ["act-3-2-1" "act-3-2-2" "act-3-2-3"]]
                                                           ["lsn-3-3" ["act-3-3-1" "act-3-3-2" "act-1-1-1"]]]]]))))
  (testing "Move 3-3-2 before 3-3-1"
    (let [actual-result (c/move-activity course-data {:source-level    3
                                                      :source-lesson   3
                                                      :source-activity 2
                                                      :target-level    3
                                                      :target-lesson   3
                                                      :target-activity 1
                                                      :position        :before})]
      (is (= (shrink-result actual-result :act) [["lvl-1" [["lsn-1-1" ["act-1-1-1" "act-1-1-2"]]
                                                           ["lsn-1-2" ["act-1-2-1" "act-1-2-2" "act-1-2-3"]]
                                                           ["lsn-1-3" ["act-1-3-1" "act-1-3-2"]]]]
                                                 ["lvl-2" [["lsn-2-1" ["act-2-1-1" "act-2-1-2"]]
                                                           ["lsn-2-2" ["act-2-2-1" "act-2-2-2" "act-2-2-3"]]]]
                                                 ["lvl-3" [["lsn-3-1" ["act-3-1-1" "act-3-1-2" "act-3-1-3"]]
                                                           ["lsn-3-2" ["act-3-2-1" "act-3-2-2" "act-3-2-3"]]
                                                           ["lsn-3-3" ["act-3-3-2" "act-3-3-1"]]]]]))))
  (testing "Move 3-3-2 before 1-1-1"
    (let [actual-result (c/move-activity course-data {:source-level    3
                                                      :source-lesson   3
                                                      :source-activity 2
                                                      :target-level    1
                                                      :target-lesson   1
                                                      :target-activity 1
                                                      :position        :before})]
      (is (= (shrink-result actual-result :act) [["lvl-1" [["lsn-1-1" ["act-3-3-2" "act-1-1-1" "act-1-1-2"]]
                                                           ["lsn-1-2" ["act-1-2-1" "act-1-2-2" "act-1-2-3"]]
                                                           ["lsn-1-3" ["act-1-3-1" "act-1-3-2"]]]]
                                                 ["lvl-2" [["lsn-2-1" ["act-2-1-1" "act-2-1-2"]]
                                                           ["lsn-2-2" ["act-2-2-1" "act-2-2-2" "act-2-2-3"]]]]
                                                 ["lvl-3" [["lsn-3-1" ["act-3-1-1" "act-3-1-2" "act-3-1-3"]]
                                                           ["lsn-3-2" ["act-3-2-1" "act-3-2-2" "act-3-2-3"]]
                                                           ["lsn-3-3" ["act-3-3-1"]]]]])))))
