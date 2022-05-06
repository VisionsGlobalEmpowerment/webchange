(ns webchange.error-message-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [webchange.state.warehouse :refer [response->errors]]))

(deftest test-getting-error-message
  (testing "Log in error"
    (let [response {:response        {:errors {:form "Invalid credentials"}}
                    :last-method     "POST"
                    :last-error      "Bad Request [400]"
                    :failure         :error
                    :status-text     "Bad Request"
                    :status          400
                    :uri             "/api/users/login"
                    :debug-message   "Http response at 400 or 500 level"
                    :last-error-code 6}]
      (let [actual-result (response->errors response)
            expected-result ["Invalid credentials"]]
        (is (= actual-result expected-result)))))

  (testing "Authentication error"
    (let [response {:response        {:errors [{:message "Unauthenticated"}]}
                    :last-method     "GET"
                    :last-error      "Unauthorized [401]"
                    :failure         :error
                    :status-text     "Unauthorized"
                    :status          401
                    :uri             "/api/users/current"
                    :debug-message   "Http response at 400 or 500 level"
                    :last-error-code 6}]
      (let [actual-result (response->errors response)
            expected-result ["Unauthenticated"]]
        (is (= actual-result expected-result))))))
