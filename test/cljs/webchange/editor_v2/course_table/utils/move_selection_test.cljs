(ns webchange.editor-v2.course-table.utils.move-selection-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [webchange.editor-v2.course-table.utils.move-selection :refer [update-selection]]))

(def columns (->> [:idx :level-idx :lesson-idx :concepts :activity :skills]
                  (map (fn [id] {:id id}))))

(def table-data [{:idx 1, :level-idx 0, :lesson-idx 0, :activity-idx 0}
                 {:idx 2, :level-idx 0, :lesson-idx 0, :activity-idx 1}
                 {:idx 3, :level-idx 0, :lesson-idx 0, :activity-idx 2}

                 {:idx 4, :level-idx 0, :lesson-idx 1, :activity-idx 0}
                 {:idx 5, :level-idx 0, :lesson-idx 1, :activity-idx 1}
                 {:idx 6, :level-idx 0, :lesson-idx 1, :activity-idx 2}

                 {:idx 7, :level-idx 1, :lesson-idx 0, :activity-idx 0}
                 {:idx 8, :level-idx 1, :lesson-idx 0, :activity-idx 1}
                 {:idx 9, :level-idx 1, :lesson-idx 0, :activity-idx 2}

                 {:idx 10, :level-idx 1, :lesson-idx 1, :activity-idx 0}
                 {:idx 11, :level-idx 1, :lesson-idx 1, :activity-idx 1}
                 {:idx 12, :level-idx 1, :lesson-idx 1, :activity-idx 2}])

(def default-selection {:level-idx    0
                        :lesson-idx   0
                        :activity-idx 0
                        :field        :skills})

(deftest test-move-selection--left
  (let [direction :left]
    (testing "move from middle position"
      (let [data {:columns    columns
                  :direction  direction
                  :table-data table-data
                  :selection  (merge default-selection
                                     {:field :concepts})}
            actual-result (update-selection data)
            expected-result (merge default-selection
                                   {:field :lesson-idx})]
        (is (= actual-result expected-result))))
    (testing "move from left position"
      (let [data {:columns    columns
                  :direction  direction
                  :table-data table-data
                  :selection  (merge default-selection
                                     {:field :idx})}
            actual-result (update-selection data)
            expected-result (merge default-selection
                                   {:field :idx})]
        (is (= actual-result expected-result))))))

(deftest test-move-selection--right
  (let [direction :right]
    (testing "move from middle position"
      (let [data {:columns    columns
                  :direction  direction
                  :table-data table-data
                  :selection  (merge default-selection
                                     {:field :concepts})}
            actual-result (update-selection data)
            expected-result (merge default-selection
                                   {:field :activity})]
        (is (= actual-result expected-result))))

    (testing "move from right position"
      (let [data {:columns    columns
                  :direction  direction
                  :table-data table-data
                  :selection  (merge default-selection
                                     {:field :skills})}
            actual-result (update-selection data)
            expected-result (merge default-selection
                                   {:field :skills})]
        (is (= actual-result expected-result))))))

(deftest test-move-selection--up
  (let [direction :up]

    ;; Level column

    (testing "move on level from 1st table row"
      (let [data {:columns    columns
                  :direction  direction
                  :table-data table-data
                  :selection  (merge default-selection
                                     {:level-idx 0, :lesson-idx 0, :activity-idx 0, :field :level-idx})}
            actual-result (update-selection data)
            expected-result (merge default-selection
                                   {:level-idx 0, :lesson-idx 0, :activity-idx 0, :field :level-idx})]
        (is (= actual-result expected-result))))

    (testing "move on level from 1st level row"
      (let [data {:columns    columns
                  :direction  direction
                  :table-data table-data
                  :selection  (merge default-selection
                                     {:level-idx 1, :lesson-idx 0, :activity-idx 0, :field :level-idx})}
            actual-result (update-selection data)
            expected-result (merge default-selection
                                   {:level-idx 0, :lesson-idx 0, :activity-idx 0, :field :level-idx})]
        (is (= actual-result expected-result))))

    (testing "move on level from middle level row"
      (let [data {:columns    columns
                  :direction  direction
                  :table-data table-data
                  :selection  (merge default-selection
                                     {:level-idx 1, :lesson-idx 1, :activity-idx 1, :field :level-idx})}
            actual-result (update-selection data)
            expected-result (merge default-selection
                                   {:level-idx 0, :lesson-idx 0, :activity-idx 0, :field :level-idx})]
        (is (= actual-result expected-result))))

    ;; Lesson column

    (testing "move on lesson from 1st table row"
      (let [data {:columns    columns
                  :direction  direction
                  :table-data table-data
                  :selection  (merge default-selection
                                     {:level-idx 0, :lesson-idx 0, :activity-idx 0, :field :lesson-idx})}
            actual-result (update-selection data)
            expected-result (merge default-selection
                                   {:level-idx 0, :lesson-idx 0, :activity-idx 0, :field :lesson-idx})]
        (is (= actual-result expected-result))))

    (testing "move on lesson from 1st level row"
      (let [data {:columns    columns
                  :direction  direction
                  :table-data table-data
                  :selection  (merge default-selection
                                     {:level-idx 1, :lesson-idx 0, :activity-idx 0, :field :lesson-idx})}
            actual-result (update-selection data)
            expected-result (merge default-selection
                                   {:level-idx 0, :lesson-idx 1, :activity-idx 0, :field :lesson-idx})]
        (is (= actual-result expected-result))))

    (testing "move on lesson from middle level row"
      (let [data {:columns    columns
                  :direction  direction
                  :table-data table-data
                  :selection  (merge default-selection
                                     {:level-idx 1, :lesson-idx 1, :activity-idx 1, :field :lesson-idx})}
            actual-result (update-selection data)
            expected-result (merge default-selection
                                   {:level-idx 1, :lesson-idx 0, :activity-idx 0, :field :lesson-idx})]
        (is (= actual-result expected-result))))

    ;; Rest columns

    (testing "move from middle position"
      (let [data {:columns    columns
                  :direction  direction
                  :table-data table-data
                  :selection  (merge default-selection
                                     {:level-idx 0, :lesson-idx 0, :activity-idx 2})}
            actual-result (update-selection data)
            expected-result (merge default-selection
                                   {:level-idx 0, :lesson-idx 0, :activity-idx 1})]
        (is (= actual-result expected-result))))

    (testing "move from 1st lesson row"
      (let [data {:columns    columns
                  :direction  direction
                  :table-data table-data
                  :selection  (merge default-selection
                                     {:level-idx 0, :lesson-idx 1, :activity-idx 0})}
            actual-result (update-selection data)
            expected-result (merge default-selection
                                   {:level-idx 0, :lesson-idx 0, :activity-idx 2})]
        (is (= actual-result expected-result))))

    (testing "move from 1st level row"
      (let [data {:columns    columns
                  :direction  direction
                  :table-data table-data
                  :selection  (merge default-selection
                                     {:level-idx 1, :lesson-idx 0, :activity-idx 0})}
            actual-result (update-selection data)
            expected-result (merge default-selection
                                   {:level-idx 0, :lesson-idx 1, :activity-idx 2})]
        (is (= actual-result expected-result))))

    (testing "move from 1st table row"
      (let [data {:columns    columns
                  :direction  direction
                  :table-data table-data
                  :selection  (merge default-selection
                                     {:level-idx 0, :lesson-idx 0, :activity-idx 0})}
            actual-result (update-selection data)
            expected-result (merge default-selection
                                   {:level-idx 0, :lesson-idx 0, :activity-idx 0})]
        (is (= actual-result expected-result))))))

(deftest test-move-selection--down
  (let [direction :down]

    ;; Level column

    (testing "move on level from last table row"
      (let [data {:columns    columns
                  :direction  direction
                  :table-data table-data
                  :selection  (merge default-selection
                                     {:level-idx 1, :lesson-idx 1, :activity-idx 2 :field :level-idx})}
            actual-result (update-selection data)
            expected-result (merge default-selection
                                   {:level-idx 1, :lesson-idx 1, :activity-idx 2 :field :level-idx})]
        (is (= actual-result expected-result))))

    (testing "move on level from last level row"
      (let [data {:columns    columns
                  :direction  direction
                  :table-data table-data
                  :selection  (merge default-selection
                                     {:level-idx 0, :lesson-idx 1, :activity-idx 2, :field :level-idx})}
            actual-result (update-selection data)
            expected-result (merge default-selection
                                   {:level-idx 1, :lesson-idx 0, :activity-idx 0, :field :level-idx})]
        (is (= actual-result expected-result))))

    (testing "move on level from middle level row"
      (let [data {:columns    columns
                  :direction  direction
                  :table-data table-data
                  :selection  (merge default-selection
                                     {:level-idx 0, :lesson-idx 0, :activity-idx 1, :field :level-idx})}
            actual-result (update-selection data)
            expected-result (merge default-selection
                                   {:level-idx 1, :lesson-idx 0, :activity-idx 0, :field :level-idx})]
        (is (= actual-result expected-result))))

    ;; Lesson column

    (testing "move on lesson from last table row"
      (let [data {:columns    columns
                  :direction  direction
                  :table-data table-data
                  :selection  (merge default-selection
                                     {:level-idx 1, :lesson-idx 1, :activity-idx 2, :field :lesson-idx})}
            actual-result (update-selection data)
            expected-result (merge default-selection
                                   {:level-idx 1, :lesson-idx 1, :activity-idx 2, :field :lesson-idx})]
        (is (= actual-result expected-result))))

    (testing "move on lesson from last level row"
      (let [data {:columns    columns
                  :direction  direction
                  :table-data table-data
                  :selection  (merge default-selection
                                     {:level-idx 0, :lesson-idx 1, :activity-idx 2, :field :lesson-idx})}
            actual-result (update-selection data)
            expected-result (merge default-selection
                                   {:level-idx 1, :lesson-idx 0, :activity-idx 0, :field :lesson-idx})]
        (is (= actual-result expected-result))))

    (testing "move on lesson from middle level row"
      (let [data {:columns    columns
                  :direction  direction
                  :table-data table-data
                  :selection  (merge default-selection
                                     {:level-idx 0, :lesson-idx 0, :activity-idx 1, :field :lesson-idx})}
            actual-result (update-selection data)
            expected-result (merge default-selection
                                   {:level-idx 0, :lesson-idx 1, :activity-idx 0, :field :lesson-idx})]
        (is (= actual-result expected-result))))

    ;; Rest columns

    (testing "move from middle position"
      (let [data {:columns    columns
                  :direction  direction
                  :table-data table-data
                  :selection  (merge default-selection
                                     {:level-idx 0, :lesson-idx 1, :activity-idx 1})}
            actual-result (update-selection data)
            expected-result (merge default-selection
                                   {:level-idx 0, :lesson-idx 1, :activity-idx 2})]
        (is (= actual-result expected-result))))

    (testing "move from last lesson row"
      (let [data {:columns    columns
                  :direction  direction
                  :table-data table-data
                  :selection  (merge default-selection
                                     {:level-idx 0, :lesson-idx 0, :activity-idx 2})}
            actual-result (update-selection data)
            expected-result (merge default-selection
                                   {:level-idx 0, :lesson-idx 1, :activity-idx 0})]
        (is (= actual-result expected-result))))

    (testing "move from last level row"
      (let [data {:columns    columns
                  :direction  direction
                  :table-data table-data
                  :selection  (merge default-selection
                                     {:level-idx 0, :lesson-idx 1, :activity-idx 2})}
            actual-result (update-selection data)
            expected-result (merge default-selection
                                   {:level-idx 1, :lesson-idx 0, :activity-idx 0})]
        (is (= actual-result expected-result))))

    (testing "move from last table row"
      (let [data {:columns    columns
                  :direction  direction
                  :table-data table-data
                  :selection  (merge default-selection
                                     {:level-idx 1, :lesson-idx 1, :activity-idx 2})}
            actual-result (update-selection data)
            expected-result (merge default-selection
                                   {:level-idx 1, :lesson-idx 1, :activity-idx 2})]
        (is (= actual-result expected-result))))))
