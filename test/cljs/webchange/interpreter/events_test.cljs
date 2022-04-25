(ns webchange.interpreter.events-test
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.test :refer-macros [deftest testing is use-fixtures]]
            [cljs.core.async :refer [<! timeout]]
            [re-frame.core :as re-frame]
            [day8.re-frame.test :refer-macros [run-test-async wait-for]]
            [webchange.events :as events]
            [webchange.interpreter.core :as ic]
            [webchange.interpreter.events :as ie]
            [webchange.fixtures :as fixtures]))

(use-fixtures :once
              {:before (fn []
                         (swap! ic/http-buffer assoc (ic/course-url "test-course") (fixtures/get-course "test-course"))
                         (swap! ic/http-buffer assoc (ic/scene-url "test-course" "initial-scene") (fixtures/get-scene "test-course" "initial-scene")))})

(use-fixtures :each {:before (fn [] (re-frame/dispatch-sync [::events/initialize-db]))})

#_(deftest state-can-be-executed
  (run-test-async
    (fixtures/init-scene)
    (wait-for [::ie/set-scene]
              (testing "object state changed"
                (re-frame/dispatch [::ie/execute-state {:target :object-with-state :id :test-state}])
                (wait-for [::ie/execute-state]
                          (is (= 100 (get-in @re-frame.db/app-db [:scenes "initial-scene" :objects :object-with-state :x]))))))))
