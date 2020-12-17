(ns webchange.editor-v2.course-table.state.data-test
  (:require [cljs.test :refer-macros [deftest testing is use-fixtures]]
            [re-frame.core :as re-frame]
            [day8.re-frame.test :refer-macros [run-test-async wait-for]]
            [webchange.events :as events]
            [webchange.interpreter.core :as ic]
            [webchange.interpreter.events :as ie]
            [webchange.fixtures :as fixtures]
            [webchange.editor-v2.course-table.state.data :as data-state]))

(use-fixtures :once
              {:before (fn []
                         (doto ic/http-buffer
                           (swap! assoc (ic/course-url "table-course") (fixtures/get-course "table-course"))
                           (swap! assoc (ic/scene-url "table-course" "scene-1") (fixtures/get-scene "table-course" "scene-1"))
                           (swap! assoc (ic/scene-url "table-course" "scene-2") (fixtures/get-scene "table-course" "scene-2"))
                           (swap! assoc (ic/lessons-url "table-course") (fixtures/get-lesson-sets "table-course"))))})

(use-fixtures :each
              {:before (fn []
                         (re-frame/dispatch-sync [::events/initialize-db]))})

(defn- get-random-item
  [list]
  (->> (count list) (rand-int) (nth list)))

(deftest course-data-prepared-properly
  (run-test-async
    (re-frame/dispatch [::data-state/init "table-course"])
    (wait-for [::ie/set-course-lessons-data]
              (let [data @(re-frame/subscribe [::data-state/table-data])]
                (testing "result is defined"
                  (is (sequential? data))
                  (is (not (empty? data))))

                (testing "rows are indexed"
                  (doseq [[idx row] (map-indexed vector data)]
                    (is (= (inc idx) (:idx row)))))

                (testing "each row has required fields"
                  (doseq [row data]
                    (is (number? (:level-idx row)))
                    (is (number? (:lesson-idx row)))
                    (is (number? (:activity-idx row)))))

                (testing "rows has correct level indexes"
                  (let [level-indexes (map :level-idx data)]
                    (is (= level-indexes [0 0 1 1 1]))))

                (testing "rows has correct lesson indexes"
                  (let [lesson-indexes (map :lesson-idx data)]
                    (is (= lesson-indexes [0 0 0 0 1]))))

                (testing "rows has correct activity indexes"
                  (let [activity-indexes (map :activity-idx data)]
                    (is (= activity-indexes [0 1 0 1 0]))))

                (testing "scene-1 has defined skills"
                  (let [scene-1-rows (filter (fn [{:keys [activity]}] (= activity "scene-1")) data)]
                    (is (not (empty? scene-1-rows)))
                    (doseq [row scene-1-rows]
                      (is (sequential? (:skills row)))
                      (doseq [skill (:skills row)]
                        (is (string? (:name skill)))
                        (is (string? (:abbr skill)))))))

                (testing "scene-2 has empty skills"
                  (let [scene-2-rows (filter (fn [{:keys [activity]}] (= activity "scene-2")) data)]
                    (is (not (empty? scene-2-rows)))
                    (doseq [row scene-2-rows]
                      (is (sequential? (:skills row)))
                      (is (empty? (:skills row))))))

                (testing "lesson set items data is defined"
                  (let [target-filter (fn [{:keys [level-idx lesson-idx]}] (and (= level-idx 0) (= lesson-idx 0)))
                        lesson-sets (->> data (filter target-filter) first :lesson-sets)]
                    (is (= lesson-sets {:concepts {:id    382 :name "ls1" :dataset-id 92
                                                   :items [{:id 2882 :name "a" :dataset-id 92}
                                                           {:id 2883 :name "b" :dataset-id 92}]}})))
                  (let [target-filter (fn [{:keys [level-idx lesson-idx]}] (and (= level-idx 1) (= lesson-idx 0)))
                        lesson-sets (->> data (filter target-filter) first :lesson-sets)]
                    (is (= lesson-sets {:current-concept {:id    382 :name "ls2" :dataset-id 92
                                                          :items [{:id 2883 :name "b" :dataset-id 92}
                                                                  {:id 2884 :name "c" :dataset-id 92}]}
                                        :all-concepts    nil})))
                  (let [target-filter (fn [{:keys [level-idx lesson-idx]}] (and (= level-idx 1) (= lesson-idx 1)))
                        lesson-sets (->> data (filter target-filter) first :lesson-sets)]
                    (is (= lesson-sets {:assessment-1 {:id    382 :name "assessment1" :dataset-id 92
                                                       :items [{:id 2882 :name "a" :dataset-id 92}
                                                               {:id 2883 :name "b" :dataset-id 92}
                                                               {:id 2884 :name "c" :dataset-id 92}]}}))))

                (testing "set tags are defined"
                  (let [target-filter (fn [{:keys [level-idx lesson-idx activity-idx]}] (and (= level-idx 0) (= lesson-idx 0) (= activity-idx 1)))
                        tags (->> data (filter target-filter) first :tags)]
                    (is (some? tags))
                    (is (= (:set-tags tags) {:intermediate [0 75], :advanced [75 101]}))))

                (testing "tags restrictions are defined"
                  (let [target-filter (fn [{:keys [level-idx lesson-idx activity-idx]}] (and (= level-idx 1) (= lesson-idx 0) (= activity-idx 0)))
                        tags (->> data (filter target-filter) first :tags)]
                    (is (some? tags))
                    (is (= (:for-tags tags) [:beginner]))))

                (testing "rows without tags are correct"
                  (let [target-filter (fn [{:keys [level-idx lesson-idx activity-idx]}] (and (= level-idx 1) (= lesson-idx 0) (= activity-idx 1)))
                        tags (->> data (filter target-filter) first :tags)]
                    (is (= tags {}))))))))
