(ns webchange.lesson-builder.stage-actions.set-action-phrase-text-test
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
    (testing "change action phrase text"
      (let [activity-id 1
            actions-path [:intro :data 0]
            phrase-text "Hello Box"]
        (mock-warehouse {(str "/api/activities/" activity-id "/current-version") activity-mock/data})
        (re-frame/dispatch [::state/init {:activity-id activity-id}])
        (rf-test/wait-for [::state/load-activity-success]
                          (re-frame/dispatch [::stage-actions/set-action-phrase-text {:action-path actions-path
                                                                                      :phrase-text phrase-text}])
                          (rf-test/wait-for [::state/set-activity-data]
                                            (let [data @(re-frame/subscribe [::state/activity-data])
                                                  diff (clojure.data/diff activity-mock/data data)]
                                              (is (= (take 2 diff) [{:actions {:intro {:data [{:data [nil {:phrase-text "Initial phrase text"}]}]}}}
                                                                    {:actions {:intro {:data [{:data [nil {:phrase-text "Hello Box"}]}]}}}])))))))))
