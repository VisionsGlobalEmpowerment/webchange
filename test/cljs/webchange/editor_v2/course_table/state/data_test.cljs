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
                           (swap! assoc (ic/scene-url "table-course" "scene-2") (fixtures/get-scene "table-course" "scene-2"))))})

(use-fixtures :each
              {:before (fn []
                         (re-frame/dispatch-sync [::events/initialize-db]))})

(defn- get-random-item
  [list]
  (->> (count list) (rand-int) (nth list)))

(deftest course-data-prepared-properly
  (run-test-async
    (fixtures/init-scene)
    (re-frame/dispatch [::data-state/init "table-course"])
    (wait-for [::ie/set-course-data]
              (wait-for [::ie/set-scenes-data]
                        (let [data @(re-frame/subscribe [::data-state/table-data])]
                          (testing "result is defined"
                            (is (sequential? data)))
                          (testing "rows are indexed"
                            (doseq [[idx row] (map-indexed vector data)]
                              (is (= (inc idx) (:idx row)))))
                          (testing "each row has required fields"
                            (doseq [row data]
                              (is (number? (:level row)))
                              (is (number? (:lesson row)))
                              (is (string? (:activity row)))))
                          (testing "scene-1 has defined skills"
                            (let [scene-1-rows (filter (fn [{:keys [activity]}] (= activity "scene-1")) data)]
                              (doseq [row scene-1-rows]
                                (is (sequential? (:skills row)))
                                (doseq [skill (:skills row)]
                                  (is (string? (:name skill)))
                                  (is (string? (:abbr skill)))))))
                          (testing "scene-2 has empty skills"
                            (let [scene-2-rows (filter (fn [{:keys [activity]}] (= activity "scene-2")) data)]
                              (doseq [row scene-2-rows]
                                (is (sequential? (:skills row)))
                                (is (empty? (:skills row)))))))))))
