(ns webchange.admin.pages.school-courses-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [re-frame.core :as re-frame]
            [day8.re-frame.test :as rf-test]
            [webchange.admin.pages.school-courses.state :as state]
            [webchange.common.warehouse :refer [mock-warehouse]]))

(def school
  {:id 1 :name "Test School"})

(def available-courses
  [{:id 1 :name "Test1" :slug "test1"}
   {:id 2 :name "Test2" :slug "test2"}])

(deftest school-courses-init
  (rf-test/run-test-async
   (testing "loading school courses"
     (let [school-id 1
           school-name-sub (re-frame/subscribe [::state/school-name])
           courses-sub (re-frame/subscribe [::state/courses])
           course-options-sub (re-frame/subscribe [::state/course-options])]
       (mock-warehouse {(str "/api/schools/" school-id) {:school school}
                        (str "/api/available-courses") available-courses
                        (str "/api/schools/" school-id "/courses") []})
       
       (re-frame/dispatch [::state/init {:school-id school-id}])
       (rf-test/wait-for [[::state/load-school-success
                           ::state/load-school-courses-success
                           ::state/load-available-courses-success]]
                         (is (= (:name school) @school-name-sub))
                         (is (= [] @courses-sub))
                         (is (every? #(some? (:text %)) @course-options-sub))
                         (is (every? #(some? (:value %)) @course-options-sub)))))))
