(ns webchange.test.auth.handler
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [webchange.test.fixtures.core :as f]
            [webchange.handler :as handler]
            [mount.core :as mount]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]
            [webchange.auth.website :as website])
  (:use clj-http.fake))

(use-fixtures :once f/init)
(use-fixtures :each f/clear-db-fixture f/with-default-school)

(def website-user-id 123)
(def website-user {:id website-user-id :email "email@example.com" :first_name "First" :last_name "Last" :image "https://example.com/image.png"})

(deftest user-can-login-with-token
         (let [token "some-token"]
           (with-global-fake-routes-in-isolation
             {(website/website-token-resource) (fn [request] {:status 200 :headers {} :body (json/write-str {:data website-user})})}
             (let [request (-> (mock/request :post "/api/users/login-token")
                               (mock/header :content-type "application/json")
                               (mock/body (json/write-str {:token token})))
                   response (handler/dev-handler request)
                   user (-> response :body (json/read-str :key-fn keyword))]
               (is (= (:first_name website-user) (:first-name user)))
               (is (= (:last_name website-user) (:last-name user)))))))

(deftest user-info-updates-on-login
  (let [{website-id :website-id} (f/website-user-created)
        new-last-name "Updated"
        new-data (assoc website-user :id website-id :last_name new-last-name)
        token "some-token"]
    (with-global-fake-routes-in-isolation
      {(website/website-token-resource) (fn [request] {:status 200 :headers {} :body (json/write-str {:data new-data})})}
      (let [request (-> (mock/request :post "/api/users/login-token")
                        (mock/header :content-type "application/json")
                        (mock/body (json/write-str {:token token})))
            response (handler/dev-handler request)
            user (-> response :body (json/read-str :key-fn keyword))]
        (is (= new-last-name (:last-name user)))))))
