(ns webchange.auth.handler
  (:require [compojure.core :refer [GET POST PUT DELETE defroutes routes]]
            [compojure.route :refer [resources not-found]]
            [ring.util.response :refer [resource-response response redirect]]
            [webchange.auth.core :as core]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [clojure.tools.logging :as log]
            [webchange.common.handler :refer [handle current-user]]))

(defn with-updated-identity [request response identity]
  (assoc response :session (merge (:session request) {:identity identity})))

(defn user->identity [user]
  (select-keys user [:id :school-id :teacher-id :student-id]))

(defn handle-login [result request]
  (let [[ok? data] result
        response {:status (if ok? 200 400) :body data}]
    (if ok?
      (with-updated-identity request response (user->identity data))
      response)))

(defroutes auth-routes
           (POST "/api/users/login" request
             (-> request :body :user core/teacher-login! (handle-login request)))
           (POST "/api/students/login" request
             (-> request :body core/student-login! (handle-login request)))
           (POST "/api/users/register-user" request
             (-> request :body :user core/register-user! handle))
           )