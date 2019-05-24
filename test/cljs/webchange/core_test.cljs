(ns webchange.core-test
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.test :refer-macros [async deftest testing is use-fixtures]]
            [cljs.core.async :refer [<! timeout]]
            [re-frame.core :as re-frame]
            [day8.re-frame.test :as rf-test]
            [webchange.core :as core]
            [webchange.events :as events]
            [webchange.interpreter.core :as ic]
            [webchange.interpreter.events :as ie]
            [webchange.fixtures :as fixtures]))

(use-fixtures :once
              {:before (fn []
                         (swap! ic/http-cache assoc (ic/course-url "test-course") (fixtures/get-course "test-course"))
                         (swap! ic/http-cache assoc (ic/scene-url "test-course" "initial-scene") (fixtures/get-scene "test-course" "initial-scene")))})

(use-fixtures :each {:before (fn [] (re-frame/dispatch-sync [::events/initialize-db]))})

(deftest example-with-timeout
  (async done
    (rf-test/run-test-async
      (testing "initial scene is set when course is loaded"
        (re-frame/dispatch [::ie/start-course "test-course"])
        (rf-test/wait-for [::ie/set-current-scene]
                          (is (= "initial-scene" (get-in @re-frame.db/app-db [:current-scene])))
                          (done))))))

(deftest scene-can-be-loaded
  (async done
    (rf-test/run-test-async
      (testing "load scene"
        (re-frame/dispatch [::ie/start-course "test-course"])
        (rf-test/wait-for [::ie/set-scene]
                          (is (= fixtures/initial-scene (get-in @re-frame.db/app-db [:scenes "initial-scene"])))
                          (done))))))
