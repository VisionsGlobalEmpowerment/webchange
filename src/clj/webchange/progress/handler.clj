(ns webchange.progress.handler
  (:require [compojure.core :refer [GET POST PUT DELETE defroutes routes]]
            [compojure.route :refer [resources not-found]]
            [ring.util.response :refer [resource-response response redirect]]
            [webchange.progress.core :as core]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [clojure.tools.logging :as log]
            [webchange.common.handler :refer [handle current-user]]
            [webchange.auth.core :as auth]
            [webchange.progress.class-profile :as class-profile]))

(defn handle-get-current-progress
  [course-id request]
  (let [owner-id (current-user request)]
    (-> (core/get-current-progress (Integer/parseInt course-id) owner-id)
        handle)))

(defn handle-get-class-profile
  [class-id course-id request]
  (let [user-id (current-user request)]
    (-> (core/get-class-profile (Integer/parseInt course-id) (Integer/parseInt class-id))
        handle)))

(defn handle-get-individual-progress
  [student-id course-id request]
  (let [user-id (current-user request)]
    (-> (core/get-individual-progress (Integer/parseInt course-id) (Integer/parseInt student-id))
        handle)))

(defn handle-save-course-progress
  [course-id request]
  (let [owner-id (current-user request)
        data (-> request :body)]
    (-> (core/save-progress! owner-id (Integer/parseInt course-id) data)
        handle)))

(defroutes progress-routes
           (GET "/api/class-profile/:class-id/course/:course-id" [class-id course-id :as request]
             (handle-get-class-profile class-id course-id request))
           (GET "/api/individual-profile/:student-id/course/:course-id" [student-id course-id :as request]
             (handle-get-individual-progress student-id course-id request))
           (GET "/api/course/:id/current-progress" [id :as request]
             (handle-get-current-progress id request))
           (POST "/api/course/:id/current-progress" [id :as request]
             (handle-save-course-progress id request)))