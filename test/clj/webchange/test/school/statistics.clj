(ns webchange.test.school.statistics
  (:require [clojure.test :refer :all]
            [webchange.test.fixtures.core :as f]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]))

(use-fixtures :once f/init)
(use-fixtures :each f/clear-db-fixture f/with-default-school)

(defn- get-default-school
  []
  (-> f/default-school-id f/get-school :body slurp (json/read-str :key-fn keyword) :school))

(deftest stats
  (testing "stats are initialized"
    (let [school (f/create-school! {:name "test" :location "test" :about "test"})
          retrieved (-> school :id f/get-school :body slurp (json/read-str :key-fn keyword) :school)]
      (is (= {:teachers 0
              :students 0
              :classes 0
              :courses 0}
             (:stats retrieved)))))
  (testing "class increase stats"
    (let [{course-id :id} (f/course-created)
          _ (f/create-class! {:name "test-name"
                              :course-id course-id})
          retrieved (get-default-school)]
      (is (= {:teachers 1 ;; teacher-logged-in creates a teacher in db
              :students 0
              :classes 1
              :courses 0}
             (:stats retrieved)))))
  (testing "student increase stats"
    (let [{class-id :id} (f/class-created)
          _ (f/create-student! {:class-id class-id
                                :first-name "first name"
                                :last-name "last name"
                                :gender 1
                                :date-of-birth "2009-01-01"
                                :access-code "test-code"})
          retrieved (get-default-school)]
      (is (= {:teachers 1
              :students 1
              :classes 2 ;;new class created for student
              :courses 0}
             (:stats retrieved))))))
