(ns webchange.lesson-builder.stage-actions.change-background-test
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
    (testing "change background to layered"
      (let [activity-id 1
            new-background-data {:type       "layered-background"
                                 :background {:src "/raw/clipart/china/china_background.png"}
                                 :surface    {:src "/raw/clipart/india/india_surface.png"}
                                 :decoration {:src "/raw/clipart/china/china_decoration.png"}}]
        (mock-warehouse {(str "/api/activities/" activity-id "/current-version") activity-mock/data})
        (re-frame/dispatch [::state/init {:activity-id activity-id}])
        (rf-test/wait-for [::state/load-activity-success]
                          (re-frame/dispatch [::stage-actions/change-background new-background-data])
                          (rf-test/wait-for [::state/set-activity-data]
                                            (let [data @(re-frame/subscribe [::state/activity-data])
                                                  diff (clojure.data/diff activity-mock/data data)]
                                              (is (= (take 2 diff) [{:objects {:background {:type "background"}}}
                                                                    {:objects {:background {:type       "layered-background"
                                                                                            :background {:src "/raw/clipart/china/china_background.png"}
                                                                                            :surface    {:src "/raw/clipart/india/india_surface.png"}
                                                                                            :decoration {:src "/raw/clipart/china/china_decoration.png"}}}}])))))))))
