(ns webchange.core-test
  (:require [cljs.test :refer-macros [deftest testing is use-fixtures]]
            [re-frame.core :as re-frame]
            [re-frame.db]
            [day8.re-frame.test :as rf-test]
            [webchange.events :as events]
            [webchange.interpreter.core :as ic]
            [webchange.interpreter.events :as ie]
            [webchange.fixtures :as fixtures]))

(use-fixtures :once
  {:before (fn []
             (doto ic/http-buffer
               (swap! assoc (ic/course-url "test-course") (fixtures/get-course "test-course"))
               (swap! assoc (ic/scene-url "test-course" "initial-scene") (fixtures/get-scene "test-course" "initial-scene"))
               (swap! assoc (ic/progress-url "test-course") (fixtures/get-progress "test-course"))))})

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
                       (is (= fixtures/initial-scene (get-in @re-frame.db/app-db [:scenes "initial-scene"])))))))

