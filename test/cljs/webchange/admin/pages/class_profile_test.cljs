(ns webchange.admin.pages.class-profile-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [re-frame.core :as re-frame]
            [day8.re-frame.test :as rf-test]
            [webchange.admin.pages.class-profile.state :as state]
            [webchange.common.warehouse :refer [mock-warehouse]]))

(deftest class-profile-init
  (rf-test/run-test-async
   (testing "loading class"
     (let [class-id 1]
       (mock-warehouse {(str "/api/classes/" class-id) {:class {:course-id 2 :name "foo"}}})
       
       (re-frame/dispatch [::state/init {:class-id class-id}])
       (rf-test/wait-for [::state/load-class-success]
                         (let [data @(re-frame/subscribe [::state/class-data])]
                           (is (= {:course-id 2 :name "foo"} data))))))))
