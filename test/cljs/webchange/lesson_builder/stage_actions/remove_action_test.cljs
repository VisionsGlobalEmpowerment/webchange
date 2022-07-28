(ns webchange.lesson-builder.stage-actions.remove-action-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [day8.re-frame.test :as rf-test]
            [webchange.lesson-builder.stage-actions.activity-mock :refer [dispatch-and-compare]]
            [webchange.lesson-builder.stage-actions :as stage-actions]))

(deftest remove-action
  (rf-test/run-test-async
    (testing "remove action in root"
      (dispatch-and-compare [::stage-actions/remove-action {:action-path [:simple-action]}]
                            [{:actions {:simple-action {:type        "animation-sequence"
                                                        :target      "guide"
                                                        :phrase-text "Phrase 1"}}}
                             nil])))

  (rf-test/run-test-async
    (testing "remove action in sequence"
      (dispatch-and-compare [::stage-actions/remove-action {:action-path [:dialog-1 :data 1]}]
                            [{:actions {:dialog-1 {:data [nil
                                                          {:type "sequence-data"
                                                           :data [{:type     "empty"
                                                                   :duration 1000}
                                                                  {:type        "animation-sequence"
                                                                   :target      "guide"
                                                                   :phrase-text "Phrase 2"}]}]}}}
                             nil])))

  (rf-test/run-test-async
    (testing "remove action in parallel"
      (dispatch-and-compare [::stage-actions/remove-action {:action-path [:dialog-2 :data 0 :data 2]}]
                            [{:actions {:dialog-2 {:data [{:data [nil
                                                                  nil
                                                                  {:type "sequence-data"
                                                                   :data [{:type     "empty"
                                                                           :duration 1000}
                                                                          {:type        "animation-sequence"
                                                                           :target      "guide"
                                                                           :phrase-text "Phrase 3"}]}]}]}}}
                             nil])))

  (rf-test/run-test-async
    (testing "remove action in parallel and simplify"
      (dispatch-and-compare [::stage-actions/remove-action {:action-path [:dialog-3 :data 0 :data 1]}]
                            [{:actions {:dialog-3 {:data [{:data [{:type "sequence-data"
                                                                   :data [{:type     "empty"
                                                                           :duration 1000}
                                                                          {:type        "animation-sequence"
                                                                           :target      "guide"
                                                                           :phrase-text "Phrase 1"}]}
                                                                  {:data [{:type     "empty"
                                                                           :duration 1000}
                                                                          {:type        "animation-sequence"
                                                                           :target      "guide"
                                                                           :phrase-text "Phrase 2"}]
                                                                   :type "sequence-data"}]
                                                           :type "parallel"}]}}}
                             {:actions {:dialog-3 {:data [{:data [{:duration 1000
                                                                   :type     "empty"}
                                                                  {:type        "animation-sequence"
                                                                   :target      "guide"
                                                                   :phrase-text "Phrase 1"}]
                                                           :type "sequence-data"}]}}}]))))
