(ns webchange.lesson-builder.stage-actions.set-object-text-test
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
    (testing "change object (keyword) text"
      (let [activity-id 1
            object-name :text-object
            object-text "New Text"]
        (mock-warehouse {(str "/api/activities/" activity-id "/current-version") activity-mock/data})
        (re-frame/dispatch [::state/init {:activity-id activity-id}])
        (rf-test/wait-for [::state/load-activity-success]
                          (re-frame/dispatch [::stage-actions/set-object-text {:object-name object-name
                                                                               :text object-text}])
                          (rf-test/wait-for [::state/set-activity-data]
                                            (let [data @(re-frame/subscribe [::state/activity-data])
                                                  diff (clojure.data/diff activity-mock/data data)]
                                              (is (= (take 2 diff) [{:objects {:text-object {:text "Letter"}}}
                                                                    {:objects {:text-object {:text "New Text"}}}]))))))))

  (rf-test/run-test-async
    (testing "change object (text) text"
      (let [activity-id 1
            object-name "text-object"
            object-text "New Text"]
        (mock-warehouse {(str "/api/activities/" activity-id "/current-version") activity-mock/data})
        (re-frame/dispatch [::state/init {:activity-id activity-id}])
        (rf-test/wait-for [::state/load-activity-success]
                          (re-frame/dispatch [::stage-actions/set-object-text {:object-name object-name
                                                                               :text object-text}])
                          (rf-test/wait-for [::state/set-activity-data]
                                            (let [data @(re-frame/subscribe [::state/activity-data])
                                                  diff (clojure.data/diff activity-mock/data data)]
                                              (is (= (take 2 diff) [{:objects {:text-object {:text "Letter"}}}
                                                                    {:objects {:text-object {:text "New Text"}}}])))))))))
