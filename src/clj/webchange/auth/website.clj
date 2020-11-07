(ns webchange.auth.website
  (:require [buddy.hashers :as hashers]
            [webchange.db.core :refer [*db*] :as db]
            [webchange.common.hmac-sha256 :as sign]
            [clojure.tools.logging :as log]
            [clj-http.client :as http]
            [config.core :refer [env]]
            [buddy.auth :as buddy]
            [webchange.auth.core :as core]))

(defn coerce-user-types
  [{id :id :as user}]
  (assoc user :id (if (int? id) id (Integer/parseInt id))))

(defn- success?
  [response]
  (and (http/success? response) (= "success" (-> response :body :status))))

(defn website-user-resource
  []
  (let [website-host (env :website-host)]
    (str "https://" website-host "/api/user")))

(defn get-user-by-id
  [website-user-id]
  (let [url (website-user-resource)
        response (http/with-additional-middleware [#'sign/wrap-apikey]
                   (http/post url {:accept :json
                                 :as :json
                                 :form-params {:user_id website-user-id}}))]
    (if (success? response)
      (-> response :body :data coerce-user-types))))

(defn website-token-resource
  []
  (let [website-host (env :website-host)]
    (str "https://" website-host "/api/authenticate")))

(defn get-user-by-token
  [token]
  (let [url (website-token-resource)
        response (http/with-additional-middleware [#'sign/wrap-apikey]
                    (http/post url {:accept :json
                                 :as :json
                                 :form-params {:token token}}))]
    (if (success? response)
      (-> response :body :data coerce-user-types))))
