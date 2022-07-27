(ns webchange.lesson-builder.stage-actions.change-background-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [day8.re-frame.test :as rf-test]
            [webchange.lesson-builder.stage-actions.activity-mock :refer [dispatch-and-compare]]
            [webchange.lesson-builder.stage-actions :as stage-actions]))

(deftest change-background
  (rf-test/run-test-async
    (testing "change background to layered"
      (dispatch-and-compare [::stage-actions/change-background {:type       "layered-background"
                                                                :background {:src "/raw/clipart/china/china_background.png"}
                                                                :surface    {:src "/raw/clipart/india/india_surface.png"}
                                                                :decoration {:src "/raw/clipart/china/china_decoration.png"}}]
                            [{:objects {:background {:type "background"}}}
                             {:objects {:background {:type       "layered-background"
                                                     :background {:src "/raw/clipart/china/china_background.png"}
                                                     :surface    {:src "/raw/clipart/india/india_surface.png"}
                                                     :decoration {:src "/raw/clipart/china/china_decoration.png"}}}}]))))
