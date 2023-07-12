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

(deftest search-accounts
  (f/live-user-created {:last-name "White"
                        :email "white.blue@example.com"})
  (f/live-user-created {:last-name "Red"
                        :email "red.green@example.com"})
  (f/live-user-created {:last-name "Grey"
                        :email "black.green@example.com"})
  (testing "without query should return all users"
    (let [{:keys [accounts total]} (-> (f/get-accounts-by-type "live" nil) :body slurp (json/read-str :key-fn keyword))]
      (is (= 3 total))
      (is (= 3 (count accounts)))))
  (testing "query should filter by last name"
    (let [q "grey"
          {:keys [accounts total]} (-> (f/get-accounts-by-type "live" q) :body slurp (json/read-str :key-fn keyword))]
      (is (= 1 total))
      (is (= 1 (count accounts)))
      (is (= "Grey" (-> accounts first :last-name)))))
  (testing "query should filter by email"
    (let [q "green"
          {:keys [accounts total]} (-> (f/get-accounts-by-type "live" q) :body slurp (json/read-str :key-fn keyword))]
      (is (= 2 total))
      (is (= 2 (count accounts))))))
