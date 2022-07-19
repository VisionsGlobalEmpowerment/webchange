(ns webchange.lesson-builder.stage-actions.remove-action-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [clojure.data :refer [diff]]
            [re-frame.core :as re-frame]
            [day8.re-frame.test :as rf-test]
            [webchange.lesson-builder.stage-actions.activity-mock :as activity-mock]
            [webchange.lesson-builder.state :as state]
            [webchange.lesson-builder.tools.stage-actions :as stage-actions]
            [webchange.common.warehouse :refer [mock-warehouse]]))

(deftest change-background
  (rf-test/run-test-async
    (testing "remove action in root"
      (let [activity-id 1
            actions-path [:simple-action]]
        (mock-warehouse {(str "/api/activities/" activity-id "/current-version") activity-mock/data})
        (re-frame/dispatch [::state/init {:activity-id activity-id}])
        (rf-test/wait-for [::state/load-activity-success]
                          (re-frame/dispatch [::stage-actions/remove-action {:action-path actions-path}])
                          (rf-test/wait-for [::state/set-activity-data]
                                            (let [data @(re-frame/subscribe [::state/activity-data])
                                                  diff (clojure.data/diff activity-mock/data data)]
                                              (is (= (take 2 diff) [{:actions {:simple-action {:type        "animation-sequence"
                                                                                               :target      "guide"
                                                                                               :phrase-text "Phrase 1"}}}
                                                                    nil]))))))))

  (rf-test/run-test-async
    (testing "remove action in sequence"
      (let [activity-id 1
            actions-path [:dialog-1 :data 1]]
        (mock-warehouse {(str "/api/activities/" activity-id "/current-version") activity-mock/data})
        (re-frame/dispatch [::state/init {:activity-id activity-id}])
        (rf-test/wait-for [::state/load-activity-success]
                          (re-frame/dispatch [::stage-actions/remove-action {:action-path actions-path}])
                          (rf-test/wait-for [::state/set-activity-data]
                                            (let [data @(re-frame/subscribe [::state/activity-data])
                                                  diff (clojure.data/diff activity-mock/data data)]
                                              (is (= (take 2 diff) [{:actions {:dialog-1 {:data [nil
                                                                                                 {:type "sequence-data"
                                                                                                  :data [{:type     "empty"
                                                                                                          :duration 1000}
                                                                                                         {:type        "animation-sequence"
                                                                                                          :target      "guide"
                                                                                                          :phrase-text "Phrase 2"}]}]}}}
                                                                    nil]))))))))

  (rf-test/run-test-async
    (testing "remove action in parallel"
      (let [activity-id 1
            actions-path [:dialog-2 :data 0 :data 2]]
        (mock-warehouse {(str "/api/activities/" activity-id "/current-version") activity-mock/data})
        (re-frame/dispatch [::state/init {:activity-id activity-id}])
        (rf-test/wait-for [::state/load-activity-success]
                          (re-frame/dispatch [::stage-actions/remove-action {:action-path actions-path}])
                          (rf-test/wait-for [::state/set-activity-data]
                                            (let [data @(re-frame/subscribe [::state/activity-data])
                                                  diff (clojure.data/diff activity-mock/data data)]
                                              (is (= (take 2 diff) [{:actions {:dialog-2 {:data [{:data [nil
                                                                                                         nil
                                                                                                         {:type "sequence-data"
                                                                                                          :data [{:type     "empty"
                                                                                                                  :duration 1000}
                                                                                                                 {:type        "animation-sequence"
                                                                                                                  :target      "guide"
                                                                                                                  :phrase-text "Phrase 3"}]}]}]}}}
                                                                    nil]))))))))

  (rf-test/run-test-async
    (testing "remove action in parallel and simplify"
      (let [activity-id 1
            actions-path [:dialog-3 :data 0 :data 1]]
        (mock-warehouse {(str "/api/activities/" activity-id "/current-version") activity-mock/data})
        (re-frame/dispatch [::state/init {:activity-id activity-id}])
        (rf-test/wait-for [::state/load-activity-success]
                          (re-frame/dispatch [::stage-actions/remove-action {:action-path actions-path}])
                          (rf-test/wait-for [::state/set-activity-data]
                                            (let [data @(re-frame/subscribe [::state/activity-data])
                                                  diff (clojure.data/diff activity-mock/data data)]
                                              (is (= (take 2 diff) [{:actions {:dialog-3 {:data [{:data [{:type "sequence-data"
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
                                                                                                  :type "sequence-data"}]}}}])))))))))
