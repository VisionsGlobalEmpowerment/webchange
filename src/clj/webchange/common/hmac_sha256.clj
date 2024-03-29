(ns webchange.common.hmac-sha256
  (:require
    [clj-http.multipart :as mp]
    [clojure.java.io :as io]
    [clojure.string :as s]
    [config.core :refer [env]]
    [clojure.tools.logging :as log])
  (:import
    (java.io ByteArrayOutputStream ByteArrayInputStream)
    (java.util Base64)
    (javax.crypto Mac)
    (javax.crypto.spec SecretKeySpec)
    (org.apache.http HttpEntity)
    (org.apache.http.entity ByteArrayEntity StringEntity)))

(defn base64-encode [to-encode]
  (.encodeToString (Base64/getEncoder) (.getBytes to-encode)))

(defn secret-key [key mac]
  (SecretKeySpec. (.getBytes key) (.getAlgorithm mac)))

(defn hmac[key string]
  (let [mac (Mac/getInstance "HMACSHA256")
        secretKey (secret-key key mac)]
    (-> (doto mac
          (.init secretKey)
          (.update (.getBytes string)))
        .doFinal)))

(defn- sign-data [api-key method uri query body nonce]
  (let [sign-str (s/join "|" [method uri query body nonce])]
    (-> (apply str (map #(format "%02x" %) (hmac api-key sign-str)))
          base64-encode)))

(defn signature-from-request [request key body]
  (let [method (s/upper-case (name (:request-method request)))
        uri (str (:uri request))
        query (str (:query-string request))
        nonce (str (get (:headers request) "apiauth-nonce"))]
    (sign-data key method uri query body nonce)))

(defn apikey-request
  [req api-key]
  (if (nil? api-key)
    req
    (let [multipart (:multipart req)
          body (or (:body req) "")
          mpe  (if multipart
                 (mp/create-multipart-entity (:multipart req) req)
                 (cond
                   (instance? HttpEntity body) body
                   (string? body) (StringEntity. ^String body "UTF-8")
                   :else (ByteArrayEntity. body)))

          body-str (with-open [xout (ByteArrayOutputStream.)]
                     (.writeTo mpe xout)
                     (str xout))
          method (s/upper-case (name (:request-method req)))
          uri (str (:uri req))
          query (str (:query-string req))
          nonce (str (quot (System/currentTimeMillis) 1000))
          result-hmac (sign-data api-key method uri query body-str nonce)]
      (-> req
          (assoc-in [:headers "apiauth-nonce"] nonce)
          (assoc-in [:headers "apiauth-signature"] result-hmac)
          (assoc-in [:body] mpe)
          (assoc-in [:multipart] nil)))))

(defn wrap-apikey
  [client]
  (fn
    ([req]
     (let [api-key (:api-key env)]
     (client (apikey-request req api-key))))
    ([req respond raise]
     (let [api-key (:api-key env)]
     (client (apikey-request req api-key) respond raise)))))

(defn slurp-bytes
  [x]
  (with-open [out (ByteArrayOutputStream.)]
    (io/copy (io/input-stream x) out)
    (.toByteArray out)))

(defn api-auth-enabled? []
  (if (contains? env :api-auth-enabled)
    (:api-auth-enabled env)
    false))

(defn get-api-key []
  (:api-key env))

(defn wrap-api-with-signature
  ([handler]
   (wrap-api-with-signature handler false))
  ([handler force]
   (fn [{{apiauth-signature "apiauth-signature"} :headers :as request}]
     (if (or force (api-auth-enabled?))
       (let [api-key (get-api-key)
             _ (.reset (:raw-body request))
             body (slurp-bytes (:raw-body request))
             _ (.reset (:raw-body request))
             body-str (slurp body)
             signature (signature-from-request request api-key body-str)]
         (if (= apiauth-signature signature)
           (handler request)
           {:status 403
            :body {:errors [{:message "Api Unauthorized"}]}}))
       (handler request)))))

(defn wrap-body-as-byte-array
  [handler]
  (fn [request]
    (if (get-in request [:headers "apiauth-signature"])
      (let [body (if (:body request) (slurp-bytes (:body request)) (.getBytes ""))
            request (-> request
                        (assoc :raw-body (ByteArrayInputStream. body))
                        (assoc :body (ByteArrayInputStream. body)))
            response (handler request)]
        response)
      (handler request))))
