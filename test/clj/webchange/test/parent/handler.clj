(ns webchange.test.parent.handler
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [webchange.test.fixtures.core :as f]
            [clojure.data.json :as json]
            [mockery.core :as mockery]
            [clojure.tools.logging :as log]
            [webchange.auth.website :as website]
            [webchange.test.course.core :as core]
            [webchange.course.core :as course]
            [webchange.db.core :refer [*db*] :as db]
            [config.core :refer [env]]
            [ring.swagger.json-schema :as js]))

(use-fixtures :once f/init)
(use-fixtures :each f/clear-db-fixture f/with-default-school)

(deftest create-student
  (testing "Child student can be created"
    (let [parent (f/website-user-created)
          result (-> (f/create-parent-student! {:name "Test" :age 3 :device "Tablet"} (:id parent))
                     :body
                     (slurp)
                     (json/read-str :key-fn keyword))]
      (is (= (:name result) "Test")))))

(deftest get-students
  (testing "Students can be found by parent"
    (let [parent (f/website-user-created)
          _ (f/create-parent-student! {:name "Test" :age 3 :device "Tablet"} (:id parent))
          result (-> (f/get-parent-students (:id parent))
                     :body
                     (slurp)
                     (json/read-str :key-fn keyword))]
      (is (= (count result) 1))
      (is (= (-> result first :name) "Test")))))

(deftest delete-student
  (testing "Student can be deleted"
    (let [parent (f/website-user-created)
          {saved-id :id} (-> (f/create-parent-student! {:name "Test" :age 3 :device "Tablet"} (:id parent))
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
          {saved-id :id} (-> (f/create-parent-student! {:name "Test" :age 3 :device "Tablet"} (:id parent))
                             :body
                             (slurp)
                             (json/read-str :key-fn keyword))
          login-result (-> (f/login-as-parent-student! {:id saved-id} (:id parent))
                           :body
                           (slurp)
                           (json/read-str :key-fn keyword))]
      (is (= (:course-slug login-result) "english")))))
