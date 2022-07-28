(ns webchange.lesson-builder.stage-actions.move-action-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [day8.re-frame.test :as rf-test]
            [webchange.lesson-builder.stage-actions.activity-mock :refer [dispatch-and-compare]]
            [webchange.lesson-builder.stage-actions :as stage-actions]))

(deftest set-action-target
  (rf-test/run-test-async
    (testing "move action toward"
      (dispatch-and-compare [::stage-actions/move-action {:source-action-path [:reorder-action-1 :data 0]
                                                          :target-action-path [:reorder-action-1 :data 2]}]
                            [{:actions {:reorder-action-1 {:data [{:data [nil {:phrase-text "Phrase 1"}]}
                                                                  {:data [nil {:phrase-text "Phrase 2"}]}]}}}
                             {:actions {:reorder-action-1 {:data [{:data [nil {:phrase-text "Phrase 2"}]}
                                                                  {:data [nil {:phrase-text "Phrase 1"}]}]}}}])))
  (rf-test/run-test-async
    (testing "move action backward"
      (dispatch-and-compare [::stage-actions/move-action {:source-action-path [:reorder-action-1 :data 2]
                                                          :target-action-path [:reorder-action-1 :data 0]}]
                            [{:actions {:reorder-action-1 {:data [{:data [nil {:phrase-text "Phrase 1"}]}
                                                                  {:data [nil {:phrase-text "Phrase 2"}]}
                                                                  {:data [nil {:phrase-text "Phrase 3"}]}]}}}
                             {:actions {:reorder-action-1 {:data [{:data [nil {:phrase-text "Phrase 3"}]}
                                                                  {:data [nil {:phrase-text "Phrase 1"}]}
                                                                  {:data [nil {:phrase-text "Phrase 2"}]}]}}}])))
  (rf-test/run-test-async
    (testing "move action to another parent"
      (dispatch-and-compare [::stage-actions/move-action {:source-action-path [:reorder-action-1 :data 2]
                                                          :target-action-path [:reorder-action-2 :data 3]}]
                            [{:actions {:reorder-action-1 {:data [nil nil
                                                                  {:type "sequence-data" :data [{:type "empty" :duration 1000}
                                                                                                {:type "animation-sequence" :target "guide" :phrase-text "Phrase 3"}]}]}}}
                             {:actions {:reorder-action-2 {:data [nil nil nil
                                                                  {:type "sequence-data" :data [{:type "empty" :duration 1000}
                                                                                                {:type "animation-sequence" :target "guide" :phrase-text "Phrase 3"}]}]}}}]))))
