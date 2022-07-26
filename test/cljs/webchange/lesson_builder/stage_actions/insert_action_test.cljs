(ns webchange.lesson-builder.stage-actions.insert-action-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [day8.re-frame.test :as rf-test]
            [webchange.lesson-builder.stage-actions.activity-mock :refer [dispatch-and-compare]]
            [webchange.lesson-builder.tools.stage-actions :as stage-actions]))

(deftest change-background
  (rf-test/run-test-async
    (testing "insert action"
      (dispatch-and-compare [::stage-actions/insert-action {:action-data      {:data "Inserted action"}
                                                            :parent-data-path [:insert-action :data]
                                                            :position         1}]
                            [{:actions {:insert-action {:data [nil
                                                               {:data [{:type     "empty"
                                                                        :duration 1000}
                                                                       {:type        "animation-sequence"
                                                                        :target      "guide"
                                                                        :phrase-text "Phrase 2"}]
                                                                :type "sequence-data"}]}}}
                             {:actions {:insert-action {:data [nil
                                                               {:data "Inserted action"}
                                                               {:type "sequence-data"
                                                                :data [{:type     "empty"
                                                                        :duration 1000}
                                                                       {:type        "animation-sequence"
                                                                        :target      "guide"
                                                                        :phrase-text "Phrase 2"}]}]}}}]))))
