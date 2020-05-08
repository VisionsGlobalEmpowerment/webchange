(ns webchange.auth.website
  (:require [buddy.hashers :as hashers]
            [webchange.db.core :refer [*db*] :as db]
            [java-time :as jt]
            [clojure.tools.logging :as log]
            [clj-http.client :as http]
            [config.core :refer [env]]
            [buddy.auth :as buddy]
            [webchange.auth.core :as core]))

(defn website-user-resource
  []
  (let [website-host (env :website-host)]
    (str "https://" website-host "/api/user")))

(defn get-user-by-id
  [website-user-id]
  (let [url (website-user-resource)
        response (http/post url {:accept :json
                                 :as :json
                                 :form-params {:user_id website-user-id}})]
    (if (http/success? response)
      (-> response :body :data))))

(defn website-token-resource
  []
  (let [website-host (env :website-host)]
    (str "https://" website-host "/api/authenticate")))

(defn get-user-by-token
  [token]
  (let [url (website-token-resource)
        response (http/post url {:accept :json
                                 :as :json
                                 :form-params {:token token}})]
    (if (http/success? response)
      (-> response :body :data))))
