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
        retrieved (-> (f/get-school-classes) :body slurp (json/read-str :key-fn keyword) :classes)]
    (is (= 1 (count retrieved)))))

(deftest class-can-be-retrieved
  (let [class (f/class-created)
        retrieved (-> class :id f/get-class :body slurp (json/read-str :key-fn keyword) :class)]
    (is (= (:name class) (:name retrieved)))))

(deftest class-can-be-created
  (let [test-name "test-name"
        {course-id :id} (f/course-created)
        class (f/create-class! {:name test-name
                                :course-id course-id})
        retrieved (-> class :id f/get-class :body slurp (json/read-str :key-fn keyword) :class)]
    (is (= test-name (:name retrieved)))))

(deftest class-can-be-updated
  (let [{class-id :id} (f/class-created)
        updated-name "edited"
        {course-id :id} (f/course-created)
        _ (f/update-class! class-id {:name updated-name :course-id course-id})
        retrieved (-> class-id f/get-class :body slurp (json/read-str :key-fn keyword) :class)]
    (is (= updated-name (:name retrieved)))))

(deftest class-can-be-deleted
  (let [{id :id} (f/class-created)
        _ (f/delete-class! id)
        status (-> id f/get-class :status)]
    (is (= 404 status))))

(deftest students-can-be-retrieved
  (let [{class-id :class-id} (f/student-created)
        students (-> class-id f/get-students :body slurp (json/read-str :key-fn keyword) :students)]
    (is (= 1 (count students)))))

(deftest unassigned-students-can-be-retrieved
  (let [_ (f/student-created {:class-id nil})
        students (-> (f/get-unassigned-students f/default-school-id) :body slurp (json/read-str :key-fn keyword) :students)]
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
        retrieved (-> class-id f/get-students :body slurp (json/read-str :key-fn keyword) :students)
        user (-> retrieved first :user)]
    (is (= 1 (count retrieved)))
    (is (= first-name (:first-name user)))
    (is (= last-name (:last-name user)))))

(deftest student-class-can-be-updated
  (let [{student-id :id class-id :class-id} (f/student-created)
        {new-class-id :id} (f/class-created)
        _ (f/update-student! student-id {:class-id new-class-id :gender 1 :date-of-birth "1999-01-01"})
        old-class-students (-> class-id f/get-students :body slurp (json/read-str :key-fn keyword) :students)
        new-class-students (-> new-class-id f/get-students :body slurp (json/read-str :key-fn keyword) :students)]
    (is (= 0 (count old-class-students)))
    (is (= 1 (count new-class-students)))))

(deftest student-access-code-can-be-updated
  (let [{student-id :id class-id :class-id} (f/student-created)
        access-code-value "new-access-code"
        _ (f/update-student! student-id {:class-id class-id :gender 1 :date-of-birth "1999-01-01" :access-code access-code-value})
        updated-student (-> student-id f/get-student :body slurp (json/read-str :key-fn keyword) :student)]
    (is (= access-code-value (:access-code updated-student)))))

(deftest student-can-be-removed-from-class
  (let [{id :id class-id :class-id} (f/student-created)
        _ (f/unassigned-student! id)
        students (-> class-id f/get-students :body slurp (json/read-str :key-fn keyword) :students)]
    (is (= 0 (count students)))))

(deftest student-become-unassigned-when-removed-from-class
  (let [{id :id} (f/student-created)
        _ (f/unassigned-student! id)
        students (-> (f/get-unassigned-students f/default-school-id) :body slurp (json/read-str :key-fn keyword) :students)]
    (is (= 1 (count students)))))

(deftest student-can-be-deleted
  (let [{id :id} (f/student-created)
        _ (f/delete-student! id)
        status (-> id f/get-student :status)]
    (is (= 404 status))))

(deftest teachers
  (let [first-name "Test"
        last-name "Test"
        email "test-teacher@example.com"
        {teacher-id :id} (f/create-teacher!
                          f/default-school-id
                          {:first-name first-name
                           :last-name last-name
                           :email email
                           :password "test-teacher"
                           :type "teacher"})
        {class-id :id} (f/class-created)]
    (testing "created teacher can be retrieved"
      (let [retrieved (-> f/default-school-id f/get-school-teachers :body slurp (json/read-str :key-fn keyword))
            user (-> retrieved last :user)]
        (is (= 1 (count retrieved)))
        (is (= first-name (:first-name user)))
        (is (= last-name (:last-name user)))
        (is (= email (:email user)))))
    (testing "teacher can be assigned to class"
      (let [_ (f/assign-teachers-to-class class-id [teacher-id])
            retrieved (-> class-id f/get-class-teachers :body slurp (json/read-str :key-fn keyword))
            class-teacher-email (-> retrieved first :user :email)]
        (is (= email class-teacher-email))))
    (testing "teacher can be removed from class"
      (let [_ (f/remove-teacher-from-class teacher-id class-id)
            retrieved (-> class-id f/get-class-teachers :body slurp (json/read-str :key-fn keyword))]
        (is (= 0 (count retrieved)))))
    (testing "teacher can be archived"
      (let [_ (f/remove-teacher teacher-id)
            retrieved (-> class-id f/get-school-teachers :body slurp (json/read-str :key-fn keyword))]
        (is (= 0 (count retrieved)))))))

(deftest teacher-transfer
  (let [teacher-email "teacher-transfer@example.com"
        _ (f/website-user-created {:email teacher-email})
        _ (f/transfer-teacher f/default-school-id {:email teacher-email})
        retrieved (-> f/default-school-id f/get-school-teachers :body slurp (json/read-str :key-fn keyword) first :user)]
    (is (= teacher-email (:email retrieved)))))

#_(deftest teacher-can-be-removed-from-class
(let [{id :id class-id :class-id} (f/student-created)
      _ (f/unassigned-student! id)
      students (-> class-id f/get-students :body slurp (json/read-str :key-fn keyword) :students)]
  (is (= 0 (count students)))))
