(ns webchange.lesson-builder.stage-actions.toggle-action-tag-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [day8.re-frame.test :as rf-test]
            [webchange.lesson-builder.stage-actions.activity-mock :refer [dispatch-and-compare]]
            [webchange.lesson-builder.stage-actions :as stage-actions]))

(deftest toggle-action-tag
  (rf-test/run-test-async
    (testing "add action tag"
      (dispatch-and-compare [::stage-actions/toggle-action-tag {:action-path [:intro]
                                                                :tag         "tag-2"}]
                            [nil
                             {:actions {:intro {:tags [nil "tag-2"]}}}])))

  (rf-test/run-test-async
    (testing "remove action tag"
      (dispatch-and-compare [::stage-actions/toggle-action-tag {:action-path [:intro]
                                                                :tag         "tag-1"}]
                            [{:actions {:intro {:tags ["tag-1"]}}}
                             {:actions {:intro {:tags nil}}}]))))

