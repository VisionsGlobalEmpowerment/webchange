(ns webchange.progress.handler
  (:require [compojure.core :refer [GET POST PUT DELETE defroutes routes]]
            [compojure.route :refer [resources not-found]]
            [ring.util.response :refer [resource-response response redirect]]
            [webchange.progress.core :as core]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [clojure.tools.logging :as log]
            [webchange.common.handler :refer [handle current-user]]
            [webchange.auth.core :as auth]
            [webchange.progress.class-profile :as class-profile]
            [webchange.progress.student-profile :as student-profile]))

(defn handle-get-current-progress
  [course-name request]
  (let [owner-id (current-user request)]
    (-> (core/get-current-progress course-name owner-id)
        handle)))

(defn handle-save-course-progress
  [course-name request]
  (let [owner-id (current-user request)
        data (-> request :body)]
    (-> (core/save-progress! owner-id course-name data)
        handle)))

(defn handle-get-class-profile
  [class-id course-name request]
  (let [user-id (current-user request)]
    (-> (core/get-class-profile course-name (Integer/parseInt class-id))
        handle)))

(defn handle-get-individual-progress
  [student-id course-name request]
  (let [user-id (current-user request)]
    (-> (core/get-individual-progress course-name (Integer/parseInt student-id))
        handle)))

(defn handle-complete-progress
  [student-id course-name request]
  (let [user-id (current-user request)
        data (-> request :body)]
    (-> (core/complete-individual-progress! course-name (Integer/parseInt student-id) data)
        handle)))

(defroutes progress-routes
           (GET "/api/class-profile/:class-id/course/:course-name" [class-id course-name :as request]
             (handle-get-class-profile class-id course-name request))
           (GET "/api/individual-profile/:student-id/course/:course-name" [student-id course-name :as request]
             (handle-get-individual-progress student-id course-name request))
           (GET "/api/courses/:id/current-progress" [id :as request]
             (handle-get-current-progress id request))
           (POST "/api/courses/:id/current-progress" [id :as request]
             (handle-save-course-progress id request))
           (PUT "/api/individual-profile/:student-id/course/:course-name/complete" [student-id course-name :as request]
             (handle-complete-progress student-id course-name request))
           )