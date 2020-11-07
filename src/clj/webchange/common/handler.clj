(ns webchange.common.handler
  (:require [buddy.auth :refer [authenticated? throw-unauthorized]]))

(defn handle
  ([result]
   (let [[ok? data] result]
     {:status (if ok? 200 400) :body data}))
  ([result on-success]
   (let [[ok? data] result
         response {:status (if ok? 200 400) :body data}]
     (if ok?
       (on-success data response)
       response))))

(defn validation-error
  [body]
  {:status  422
   :headers {}
   :body    {:errors body}})

(defn current-user
  [request]
  (if-not (authenticated? request)
    (throw-unauthorized)
    (-> request :session :identity :id)))

(defn current-school
  [request]
  (if-not (authenticated? request)
    (throw-unauthorized)
    (-> request :session :identity :school-id)))