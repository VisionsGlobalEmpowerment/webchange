(ns webchange.test.accounts.handler
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [webchange.test.fixtures.core :as f]
            [webchange.handler :as handler]
            [mount.core :as mount]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]
            [webchange.emails.core :as emails]))

(use-fixtures :once f/init)
(use-fixtures :each f/clear-db-fixture f/with-default-school)

(deftest registration
  (let [data {:firstname "first name"
              :lastname "last name"
              :email "test-registration@example.com"
              :password "test"}]
    (testing "successful regisrtation redirects to success page"
      (with-redefs [emails/request-email-confirmation! (fn [_] nil)]
        (let [request (-> (mock/request :post "/accounts/registration")
                          (mock/header :content-type "application/json")
                          (mock/body data))
              response (handler/dev-handler request)]
          (is (= 302 (:status response)))
          (is (= "/accounts/sign-up-success" (-> response :headers (get "Location")))))))))
