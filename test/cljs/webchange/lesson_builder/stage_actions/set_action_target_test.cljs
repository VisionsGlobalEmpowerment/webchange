(ns webchange.lesson-builder.stage-actions.set-action-target-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [day8.re-frame.test :as rf-test]
            [webchange.lesson-builder.stage-actions.activity-mock :refer [dispatch-and-compare]]
            [webchange.lesson-builder.tools.stage-actions :as stage-actions]))

(deftest set-action-target
  (rf-test/run-test-async
    (testing "change action target"
      (dispatch-and-compare [::stage-actions/set-action-target {:action-path [:intro :data 0]
                                                                :target      "box1"}]
                            [{:actions {:intro {:data [{:data [nil {:target "guide"}]}]}}}
                             {:actions {:intro {:data [{:data [nil {:target "box1"}]}]}}}]))))
