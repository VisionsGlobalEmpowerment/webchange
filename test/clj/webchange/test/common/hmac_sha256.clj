(ns webchange.test.common.hmac-sha256
  (:require [clojure.test :refer :all]
            [webchange.common.hmac-sha256 :as hmac]
            [mockery.core :as mockery]
            ))

(deftest test-middleware-allow
  (mockery/with-mocks[_ {:target :webchange.common.hmac-sha256/api-auth-enabled? :return true}
                      _ {:target :webchange.common.hmac-sha256/get-api-key :return "123456"}]
                     (let [middleware (hmac/wrap-api-with-signature (fn [request] "OK"))
                           request {:headers {"apiauth-signature" "ZWI0MDNiNzg4MzM5YjM4NmE1OWI2OTk2MWNhMmExOTAyZjNhNDQ0MzJiYzdlYWU2NGY5ZGMzZDRkMzA0NDU2ZA=="
                                           "apiauth-nonce" 10213741}
                                 :raw-body (java.io.ByteArrayInputStream. (.getBytes ""))
                                 :request-method :get
                                 :uri "test/test1"
                                 :query-string "hello=kitty"}
                        response (middleware request)]
                       (is (= response "OK")))))

(deftest test-middleware-not-allow
  (mockery/with-mocks[_ {:target :webchange.common.hmac-sha256/api-auth-enabled? :return true}
                      _ {:target :webchange.common.hmac-sha256/get-api-key :return "123456"}]
                     (let [middleware (hmac/wrap-api-with-signature (fn [request] "OK"))
                           request {:headers {"apiauth-signature" "wrong-signature"
                                           "apiauth-nonce" 10213741}
                                 :raw-body (java.io.ByteArrayInputStream. (.getBytes ""))
                                 :request-method :get
                                 :uri "test/test1"
                                 :query-string "hello=kitty"}
                        response (middleware request)]
                       (is (= (:status response) 403)))))

(deftest test-client-middleware
  (let [request {:body " "
                 :request-method :get
                 :uri "test/test1"
                 :query-string "hello=kitty"}
        request (hmac/apikey-request request "123456")]
    (is (get (:headers request) "apiauth-signature"))
    (is (get (:headers request) "apiauth-nonce"))))