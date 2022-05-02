(ns webchange.admin.schools.data-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [re-frame.core :as re-frame]
            [day8.re-frame.test :as rf-test]
            [webchange.admin.pages.schools.state :as schools-state]
            [webchange.common.warehouse :refer [warehouse]]))

(deftest schools-can-be-loaded
  (rf-test/run-test-async
    (testing "loading schools"
      (warehouse {"/api/schools" {:schools [{:id 1, :name "foo"}
                                            {:id 2, :name "bar"}]}})
      (re-frame/dispatch [::schools-state/init])

      (rf-test/wait-for [::schools-state/set-schools]
                        (let [data @(re-frame/subscribe [::schools-state/schools-list])]
                          (is (= {1 {:id 1, :name "foo"}
                                  2 {:id 2, :name "bar"}}
                                 data)))))))
