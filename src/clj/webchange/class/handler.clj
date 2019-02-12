(ns webchange.class.handler
  (:require [compojure.core :refer [GET POST PUT DELETE defroutes routes]]
            [compojure.route :refer [resources not-found]]
            [ring.util.response :refer [resource-response response redirect]]
            [webchange.class.core :as core]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [clojure.tools.logging :as log]
            [webchange.common.handler :refer [handle current-user]]
            [webchange.auth.core :as auth]))

(defn handle-create-class
  [request]
  (let [owner-id (current-user request)
        data (-> request :body)]
    (-> (core/create-class! data)
        handle)))

(defn handle-update-class
  [id request]
  (let [owner-id (current-user request)
        data (-> request :body)]
    (-> (core/update-class! (Integer/parseInt id) data)
        handle)))

(defn handle-delete-class
  [id request]
  (log/warn "delete! " id)
  (let [owner-id (current-user request)]
    (-> (core/delete-class! (Integer/parseInt id))
        handle)))

(defn handle-create-student
  [request]
  (let [owner-id (current-user request)
        data (-> request :body)
        [{user-id :id}] (-> data auth/prepare-register-data auth/create-user!)]
    (auth/activate-user! user-id)
    (-> (core/create-student! {:user-id user-id :class-id (:class-id data)})
        handle)))

(defn handle-update-student
  [id request]
  (let [owner-id (current-user request)
        data (-> request :body)]
    (-> (core/update-student! (Integer/parseInt id) data)
        handle)))

(defn handle-delete-student
  [id request]
  (let [owner-id (current-user request)]
    (-> (core/delete-student! (Integer/parseInt id))
        handle)))

(defroutes class-routes
           (GET "/api/classes" [] (-> (core/get-classes) response))
           (GET "/api/classes/:id" [id]
             (if-let [item (-> id Integer/parseInt core/get-class)]
               (response {:class item})
               (not-found "not found")))
           (POST "/api/classes" request
             (handle-create-class request))
           (PUT "/api/classes/:id" [id :as request]
             (handle-update-class id request))
           (DELETE "/api/classes/:id" [id :as request]
             (handle-delete-class id request))

           (GET "/api/classes/:id/students" [id] (-> id Integer/parseInt core/get-students-by-class response))
           (GET "/api/students/:id" [id] (-> id Integer/parseInt core/get-student response))
           (POST "/api/students" request
             (handle-create-student request))
           (PUT "/api/students/:id" [id :as request]
             (handle-update-student id request))
           (DELETE "/api/students/:id" [id :as request]
             (handle-delete-student id request))
           )