(ns webchange.lesson-builder.stage-actions.set-action-phrase-text-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [day8.re-frame.test :as rf-test]
            [webchange.lesson-builder.stage-actions.activity-mock :refer [dispatch-and-compare]]
            [webchange.lesson-builder.stage-actions :as stage-actions]))

(deftest set-action-phrase-text
  (rf-test/run-test-async
    (testing "change action phrase text"
      (dispatch-and-compare [::stage-actions/set-action-phrase-text {:action-path [:intro :data 0]
                                                                     :phrase-text "Hello Box"}]
                            [{:actions {:intro {:data [{:data [nil {:phrase-text "Initial phrase text"}]}]}}}
                             {:actions {:intro {:data [{:data [nil {:phrase-text "Hello Box"}]}]}}}]))))
