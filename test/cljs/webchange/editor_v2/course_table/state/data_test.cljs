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
                         (swap! ic/http-buffer assoc (ic/course-url "table-course") (fixtures/get-course "table-course")))})

(use-fixtures :each
              {:before (fn []
                         (re-frame/dispatch-sync [::events/initialize-db]))})

(deftest course-data-prepared-properly
  (run-test-async
    (fixtures/init-scene)
    (re-frame/dispatch [::data-state/init "table-course"])
    (wait-for [::ie/set-course-data]
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
                    (is (string? (:activity row)))))))))
