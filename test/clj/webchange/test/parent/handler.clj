(ns webchange.test.parent.handler
  (:require [clojure.test :refer :all]
            [webchange.test.fixtures.core :as f]
            [clojure.data.json :as json]))

(use-fixtures :once f/init)
(use-fixtures :each f/clear-db-fixture f/with-default-school)

(deftest create-student
  (testing "Child student can be created"
    (let [parent (f/website-user-created)
          result (-> (f/create-parent-student! {:name "Test" :date-of-birth "2010-12-31" :course-slug "english" :device "Tablet"} (:id parent))
                     :body
                     (slurp)
                     (json/read-str :key-fn keyword))]
      (is (= (:name result) "Test")))))

(deftest get-students
  (testing "Students can be found by parent"
    (let [parent (f/website-user-created)
          _ (f/create-parent-student! {:name "Test" :date-of-birth "2010-12-31" :device "Tablet" :course-slug "english"} (:id parent))
          result (-> (f/get-parent-students (:id parent))
                     :body
                     (slurp)
                     (json/read-str :key-fn keyword))]
      (is (= (count result) 1))
      (is (= (-> result first :name) "Test")))))

(deftest delete-student
  (testing "Student can be deleted"
    (let [parent (f/website-user-created)
          {saved-id :id} (-> (f/create-parent-student! {:name "Test" :date-of-birth "2010-12-31" :device "Tablet" :course-slug "english"} (:id parent))
                             :body
                             (slurp)
                             (json/read-str :key-fn keyword))
          _ (f/delete-parent-student! saved-id (:id parent))
          result (-> (f/get-parent-students (:id parent))
                     :body
                     (slurp)
                     (json/read-str :key-fn keyword))]
      (is (= (count result) 0)))))

(deftest login-as
  (testing "Parent can login as child"
    (let [parent (f/website-user-created)
          {saved-id :id} (-> (f/create-parent-student! {:name "Test" :date-of-birth "2010-12-31" :device "Tablet" :course-slug "english"} (:id parent))
                             :body
                             (slurp)
                             (json/read-str :key-fn keyword))
          login-result (-> (f/login-as-parent-student! {:id saved-id} (:id parent))
                           :body
                           (slurp)
                           (json/read-str :key-fn keyword))]
      (is (= (:course-slug login-result) "english")))))
