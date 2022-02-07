(ns webchange.auth.handler
  (:require [compojure.core :refer [GET POST PUT DELETE defroutes routes]]
            [compojure.route :refer [resources not-found]]
            [ring.util.response :refer [resource-response response redirect]]
            [webchange.auth.core :as core]
            [webchange.auth.website :as website]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [clojure.tools.logging :as log]
            [webchange.common.handler :refer [handle current-user current-school]]))

(defn handle-login [result request]
  (let [[ok? data] result
        response {:status (if ok? 200 400) :body data}]
    (if ok?
      (core/with-updated-identity request response (core/user->identity data))
      response)))

(defn handle-token-login
  [request]
  (let [token (-> request :body :token)
        user (some-> token
                     website/get-user-by-token
                     core/replace-user-from-website!)
        result (if user
                 [true user]
                 [false {:message "User not found"}])]
    (handle-login result request)))

(defn handle-current-user
  [request]
  (let [user-id (current-user request)
        school-id (current-school request)]
    (-> (core/get-current-user user-id school-id)
        handle)))

(defroutes auth-routes
  (POST "/api/users/login" request
        (-> request :body :user core/teacher-login! (handle-login request)))
  (POST "/api/users/login-token" request
        (handle-token-login request))
  (POST "/api/students/login" request
        (-> request
            :body
            core/student-login!
            (handle-login request)
            (assoc-in [:cookies :parent-login] {:value false
                                                :path "/"
                                                :http-only true})))
  (GET "/api/users/current" request (handle-current-user request))
  (POST "/api/users/register-user" request
        (-> request :body :user core/register-user! handle))

  (GET "/user/profile" [] (redirect (website/website-profile-page)))
  (GET "/user/courses" [] (redirect (website/website-courses-page)))
  (GET "/user/login" [] (redirect (website/website-login-page)))
  (GET "/user/logout" [] (redirect (website/website-logout-page))))
