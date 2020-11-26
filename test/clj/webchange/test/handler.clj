(ns webchange.test.handler
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [webchange.test.fixtures.core :as f]
            [webchange.handler :as handler]
            [mount.core :as mount]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]))

(use-fixtures :once f/init)
(use-fixtures :each f/clear-db-fixture f/with-default-school)

(deftest teacher-can-log-in
  (let [user (f/teacher-user-created)
        _ (f/teacher-created {:user-id (:id user)})
        credentials {:user (select-keys user [:email :password])}
        request (-> (mock/request :post "/api/users/login")
                    (mock/header :content-type "application/json")
                    (mock/body (json/write-str credentials)))
        response (handler/dev-handler request)
        body (-> response :body slurp (json/read-str :key-fn keyword))]
    (is (= 200 (:status response)))
    (is (= (:email user) (:email body)))))

(deftest student-can-log-in
  (let [student (f/student-created)
        credentials {:access-code (:access-code student) :school-id f/default-school-id}
        request (-> (mock/request :post "/api/students/login")
                    (mock/header :content-type "application/json")
                    (mock/body (json/write-str credentials)))
        response (handler/dev-handler request)
        body (-> response :body slurp (json/read-str :key-fn keyword))]
    (is (= 200 (:status response)))
    (is (= (:user-id student) (:id body)))
    (is (= (:school-id student) (:school-id body)))))

(deftest unauthorized-cannot-access-editor
  (let [request (mock/request :get "/courses/test/editor")
        response (handler/dev-handler request)]
    (is (= 302 (:status response)))))
