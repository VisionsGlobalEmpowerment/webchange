(ns webchange.test.school.handler
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [webchange.test.fixtures.core :as f]
            [webchange.handler :as handler]
            [mount.core :as mount]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]))

(use-fixtures :once f/init)
(use-fixtures :each f/clear-db-fixture f/with-default-school)

(deftest school-can-be-created
  (let [test-name "test-name"
        school (f/create-school! {:name test-name})
        retrieved (-> school :id f/get-school :body (json/read-str :key-fn keyword) :school)
        ]
    (is (= test-name (:name retrieved)))))

(deftest schools-can-be-retrieved
  (let [retrieved (-> (f/get-schools) :body (json/read-str :key-fn keyword) :schools)]
    (is (= 1 (count retrieved)))))

(deftest school-can-be-updated
  (let [test-name "test-name"
        {school-id :id} (f/create-school! {:name test-name})
        updated-name "edited"
        _ (f/update-school! school-id {:name updated-name})
        retrieved (-> school-id f/get-school :body (json/read-str :key-fn keyword) :school)]
    (is (= updated-name (:name retrieved)))))

(deftest school-can-be-deleted
  (let [test-name "test-name"
        {id :id} (f/create-school! {:name test-name})
        _ (f/delete-school! id)
        status (-> id f/get-school :status)]
    (is (= 404 status))))

(deftest current-school-can-be-retrieved
  (let [school (-> (f/get-current-school) :body (json/read-str :key-fn keyword))]
    (is (= f/default-school-id (:id school)))))
