(ns webchange.admin.pages.add-school-test
  (:require [cljs.test :refer-macros [deftest testing is use-fixtures]]
            [re-frame.core :as re-frame]
            [day8.re-frame.test :as rf-test]
            [webchange.admin.pages.add-school.state :as state]
            [webchange.admin.routes :as routes]
            [webchange.common.warehouse :refer [mock-warehouse]]))

(use-fixtures :once
  {:before (fn [] (routes/init! "/admin/schools/add"))})

#_(deftest school-can-be-created
  (rf-test/run-test-async
   (testing "school can be created with correct data"
     (let [errors (re-frame/subscribe [::state/errors])]
       (mock-warehouse {"/api/schools" {:id 1}})
       
       (re-frame/dispatch [::state/create-school {:name "test name"
                                                  :location "test location"
                                                  :about "test about"}])
       (rf-test/wait-for [::state/create-school-success]
                         (is (empty? @errors)))))))


#_(deftest validation
  (rf-test/run-test-sync
   (let [errors (re-frame/subscribe [::state/errors])]
     (mock-warehouse {"/api/schools" {:id 1}})
     (testing "name is required"  
       (re-frame/dispatch [::state/create-school {:name ""
                                                  :location "test location"
                                                  :about "test about"}])
       (is (seq @errors))))))
