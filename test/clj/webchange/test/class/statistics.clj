(ns webchange.test.class.statistics
  (:require [clojure.test :refer :all]
            [webchange.test.fixtures.core :as f]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]))

(use-fixtures :once f/init)
(use-fixtures :each f/clear-db-fixture f/with-default-school)

(deftest stats
  (let [{course-id :id} (f/course-created)
        {class-id :id} (f/create-class! {:name "test class"
                                         :course-id course-id})]
    (testing "stats are initialized"
      (let [retrieved (-> class-id f/get-class :body slurp (json/read-str :key-fn keyword) :class)]
        (is (= {:teachers 0
                :students 0}
               (:stats retrieved)))))
    (testing "student increase stats"
      (let [_ (f/create-student! {:class-id class-id
                                  :first-name "first name"
                                  :last-name "last name"
                                  :gender 1
                                  :date-of-birth "2009-01-01"
                                  :access-code "test-code"})
            retrieved (-> class-id f/get-class :body slurp (json/read-str :key-fn keyword) :class)]
        (is (= {:teachers 0
                :students 1}
               (:stats retrieved)))))
    (testing "unassign student decrease stats"
      (let [student-id (-> class-id f/get-students :body slurp (json/read-str :key-fn keyword) :students first :id)
            _ (f/unassigned-student! student-id)
            retrieved (-> class-id f/get-class :body slurp (json/read-str :key-fn keyword) :class)]
        (is (= {:teachers 0
                :students 0}
               (:stats retrieved)))))
    (testing "archive student decrease stats"
      (let [{student-id :id} (f/create-student! {:class-id class-id
                                                 :first-name "first name"
                                                 :last-name "last name"
                                                 :gender 1
                                                 :date-of-birth "2009-01-01"
                                                 :access-code "test-code-2"})
            _ (f/archive-student! student-id)
            retrieved (-> class-id f/get-class :body slurp (json/read-str :key-fn keyword) :class)]
        (is (= {:teachers 0
                :students 0}
               (:stats retrieved)))))))
