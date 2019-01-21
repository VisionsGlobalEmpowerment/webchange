(ns webchange.test.handler
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [webchange.test.fixtures.core :as f]
            [webchange.handler :as handler]
            [mount.core :as mount]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]))

(use-fixtures :once f/init)
(use-fixtures :each f/clear-db-fixture)

(deftest user-can-log-in
  (let [user (f/user-created)
        credentials {:user (select-keys user [:email :password])}
        request (-> (mock/request :post "/api/users/login")
                    (mock/header :content-type "application/json")
                    (mock/body (json/write-str credentials)))
        response (handler/dev-handler request)
        body (json/read-str (:body response))]
    (is (= 200 (:status response)))
    (is (= (:email user) (get-in body ["email"])))))

(deftest unauthorized-cannot-access-editor
  (let [request (mock/request :get "/editor")
        response (handler/dev-handler request)]
    (is (= 302 (:status response)))))

(deftest logged-in-user-can-access-editor
  (let [request (-> (mock/request :get "/editor")
                    f/user-logged-in)
        response (handler/dev-handler request)]
    (is (= 200 (:status response)))))

(deftest course-can-be-retrieved
  (let [course (f/course-created)
        course-url (str "/api/courses/" (:name course))
        request (-> (mock/request :get course-url)
                    f/user-logged-in)
        response (handler/dev-handler request)
        body (json/read-str (:body response))]
    (is (= 200 (:status response)))
    (is (= (get-in course [:data :initial-scene]) (get body "initial-scene")))))

(deftest scene-can-be-retrieved
  (let [scene (f/scene-created)
        scene-url (str "/api/courses/" (:course-name scene) "/scenes/" (:name scene))
        request (-> (mock/request :get scene-url)
                    f/user-logged-in)
        response (handler/dev-handler request)]
    (is (= 200 (:status response)))
    (is (= (get-in scene [:data :test]) (-> response :body json/read-str (get "test"))))))