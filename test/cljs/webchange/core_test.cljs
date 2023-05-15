(ns webchange.core-test
  (:require [cljs.test :refer-macros [deftest testing is use-fixtures]]
            [re-frame.core :as re-frame]
            [re-frame.db]
            [day8.re-frame.test :as rf-test]
            [webchange.events :as events]
            [webchange.interpreter.events :as ie]
            [webchange.fixtures :as fixtures]
            [webchange.common.warehouse :refer [mock-warehouse]]))

(use-fixtures :once
  {:before (fn []
             (mock-warehouse {"/api/courses/test-course" (fixtures/get-course "test-course")
                              "/api/activities/1/current-version" (fixtures/get-scene 1)
                              "/api/courses/test-course/current-progress" (fixtures/get-progress "test-course")}))})

(use-fixtures :each {:before (fn []
                               (re-frame/dispatch-sync [::events/initialize-db]))})

(deftest course-can-be-loaded
  (rf-test/run-test-async
   (testing "initial scene is set when course is loaded"
     (re-frame/dispatch [::ie/start-course "test-course"])
     (rf-test/wait-for [::ie/set-scene]
                       (is (= 1 (get-in @re-frame.db/app-db [:current-scene])))))))

(deftest scene-can-be-loaded
  (rf-test/run-test-async
   (testing "load scene"
     (re-frame/dispatch [::ie/start-course "test-course"])
     (rf-test/wait-for [::ie/set-scene]
                       (is (= (fixtures/get-scene 1) (get-in @re-frame.db/app-db [:scenes 1])))))))

