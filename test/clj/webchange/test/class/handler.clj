(ns webchange.test.class.handler
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [webchange.test.fixtures.core :as f]
            [webchange.handler :as handler]
            [mount.core :as mount]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]))

(use-fixtures :once f/init)
(use-fixtures :each f/clear-db-fixture)

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

(deftest student-can-be-created
  (let [{user-id :id} (f/user-created)
        {class-id :id} (f/class-created)
        _ (f/create-student! {:class-id class-id :user-id user-id})
        retrieved (-> class-id f/get-students :body (json/read-str :key-fn keyword) :students)]
    (is (= 1 (count retrieved)))))

(deftest student-can-be-updated
  (let [{student-id :id class-id :class-id} (f/student-created)
        {new-class-id :id} (f/class-created)
        _ (f/update-student! student-id {:class-id new-class-id})
        old-class-students (-> class-id f/get-students :body (json/read-str :key-fn keyword) :students)
        new-class-students (-> new-class-id f/get-students :body (json/read-str :key-fn keyword) :students)]
    (is (= 0 (count old-class-students)))
    (is (= 1 (count new-class-students)))))

(deftest student-can-be-deleted
  (let [{id :id class-id :class-id} (f/student-created)
        _ (f/delete-student! id)
        students (-> class-id f/get-students :body (json/read-str :key-fn keyword) :students)]
    (is (= 0 (count students)))))