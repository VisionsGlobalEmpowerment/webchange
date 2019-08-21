(ns webchange.test.class.handler
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [webchange.test.fixtures.core :as f]
            [webchange.handler :as handler]
            [mount.core :as mount]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]))

(use-fixtures :once f/init)
(use-fixtures :each f/clear-db-fixture f/with-default-school)

(deftest classes-can-be-retrieved
  (let [_ (f/class-created)
        retrieved (-> (f/get-classes) :body (json/read-str :key-fn keyword) :classes)]
    (is (= 1 (count retrieved)))))

(deftest class-can-be-retrieved
  (let [class (f/class-created)
        retrieved (-> class :id f/get-class :body (json/read-str :key-fn keyword) :class)]
    (is (= (:name class) (:name retrieved)))))

(deftest class-can-be-created
  (let [test-name "test-name"
        class (f/create-class! {:name test-name})
        retrieved (-> class :id f/get-class :body (json/read-str :key-fn keyword) :class)]
    (is (= test-name (:name retrieved)))))

(deftest class-can-be-updated
  (let [{class-id :id} (f/class-created)
        updated-name "edited"
        _ (f/update-class! class-id {:name updated-name})
        retrieved (-> class-id f/get-class :body (json/read-str :key-fn keyword) :class)]
    (is (= updated-name (:name retrieved)))))

(deftest class-can-be-deleted
  (let [{id :id} (f/class-created)
        _ (f/delete-class! id)
        status (-> id f/get-class :status)]
    (is (= 404 status))))

(deftest students-can-be-retrieved
  (let [{class-id :class-id} (f/student-created)
        students (-> class-id f/get-students :body (json/read-str :key-fn keyword) :students)]
    (is (= 1 (count students)))))

(deftest unassigned-students-can-be-retrieved
  (let [_ (f/student-created {:class-id nil})
        students (-> (f/get-unassigned-students) :body (json/read-str :key-fn keyword) :students)]
    (is (= 1 (count students)))))

(deftest student-can-be-created
  (let [{class-id :id} (f/class-created)
        first-name "Test"
        last-name "Test"
        _ (f/create-student! {:class-id class-id
                              :first-name first-name
                              :last-name last-name
                              :gender 1
                              :date-of-birth "2009-01-01"
                              :access-code "test-code"})
        retrieved (-> class-id f/get-students :body (json/read-str :key-fn keyword) :students)
        user (-> retrieved first :user)]
    (is (= 1 (count retrieved)))
    (is (= first-name (:first-name user)))
    (is (= last-name (:last-name user)))))

(deftest student-class-can-be-updated
  (let [{student-id :id class-id :class-id} (f/student-created)
        {new-class-id :id} (f/class-created)
        _ (f/update-student! student-id {:class-id new-class-id :gender 1 :date-of-birth "1999-01-01"})
        old-class-students (-> class-id f/get-students :body (json/read-str :key-fn keyword) :students)
        new-class-students (-> new-class-id f/get-students :body (json/read-str :key-fn keyword) :students)]
    (is (= 0 (count old-class-students)))
    (is (= 1 (count new-class-students)))))

(deftest student-access-code-can-be-updated
  (let [{student-id :id class-id :class-id} (f/student-created)
        access-code-value "new-access-code"
        _ (f/update-student! student-id {:class-id class-id :gender 1 :date-of-birth "1999-01-01" :access-code access-code-value})
        updated-student (-> student-id f/get-student :body (json/read-str :key-fn keyword) :student)]
    (is (= access-code-value (:access-code updated-student)))))

(deftest student-can-be-removed-from-class
  (let [{id :id class-id :class-id} (f/student-created)
        _ (f/unassigned-student! id)
        students (-> class-id f/get-students :body (json/read-str :key-fn keyword) :students)]
    (is (= 0 (count students)))))

(deftest student-become-unassigned-when-removed-from-class
  (let [{id :id} (f/student-created)
        _ (f/unassigned-student! id)
        students (-> (f/get-unassigned-students) :body (json/read-str :key-fn keyword) :students)]
    (is (= 1 (count students)))))

(deftest student-can-be-deleted
  (let [{id :id} (f/student-created)
        _ (f/delete-student! id)
        status (-> id f/get-student :status)]
    (is (= 404 status))))

(deftest current-school-can-be-retrieved
  (let [school (-> (f/get-current-school) :body (json/read-str :key-fn keyword))]
    (is (= f/default-school-id (:id school)))))