(ns webchange.test.auth.handler
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [webchange.test.fixtures.core :as f]
            [webchange.handler :as handler]
            [clojure.data.json :as json]
            [webchange.auth.website :as website]
            [clj-http.fake :refer [with-global-fake-routes-in-isolation]]))

(use-fixtures :once f/init)
(use-fixtures :each f/clear-db-fixture f/with-default-school)

(deftest user-can-login-with-token
  (let [token "some-token"]
    (with-global-fake-routes-in-isolation
      {(website/website-token-resource) (fn [_request] {:status 200 :headers {} :body (json/write-str {:status "success" :data f/website-user})})}
      (let [request (-> (mock/request :post "/api/users/login-token")
                        (mock/header :content-type "application/json")
                        (mock/body (json/write-str {:token token})))
            response (handler/dev-handler request)
            user (-> response :body slurp (json/read-str :key-fn keyword))]
        (is (= (:first_name f/website-user) (:first-name user)))
        (is (= (:last_name f/website-user) (:last-name user)))))))

(deftest user-info-updates-on-login
  (let [{website-id :website-id} (f/website-user-created)
        new-last-name "Updated"
        new-data (assoc f/website-user :id website-id :last_name new-last-name)
        token "some-token"]
    (with-global-fake-routes-in-isolation
      {(website/website-token-resource) (fn [_request] {:status 200 :headers {} :body (json/write-str {:status "success" :data new-data})})}
      (let [request (-> (mock/request :post "/api/users/login-token")
                        (mock/header :content-type "application/json")
                        (mock/body (json/write-str {:token token})))
            response (handler/dev-handler request)
            user (-> response :body slurp (json/read-str :key-fn keyword))]
        (is (= new-last-name (:last-name user)))))))
