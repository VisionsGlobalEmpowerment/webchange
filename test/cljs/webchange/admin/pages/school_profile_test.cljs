(ns webchange.admin.pages.school-profile-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [re-frame.core :as re-frame]
            [day8.re-frame.test :as rf-test]
            [webchange.admin.pages.school-profile.state :as state]
            [webchange.common.warehouse :refer [mock-warehouse]]))

(def default-school-data {:id 1 :name "Test School" :location "Test Location" :about "Lorem ipsum"
                          :stats {:teachers 0
                                  :students 0
                                  :classes 0
                                  :courses 0}})
(deftest school-profile-init
  (rf-test/run-test-async
   (testing "loading class"
     (let [school-id 1]
       (mock-warehouse {(str "/api/schools/" school-id) {:school default-school-data}})
       
       (re-frame/dispatch [::state/init {:school-id school-id}])
       (rf-test/wait-for [::state/set-school-data]
                         (let [data @(re-frame/subscribe [::state/school-data])]
                           (is (= default-school-data data))))))))
