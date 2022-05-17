(ns webchange.progress.handler
  (:require 
    [compojure.api.sweet :refer [GET POST PUT DELETE defroutes routes]]
    [ring.util.response :refer [response]]
    [webchange.progress.core :as core]
    [clojure.tools.logging :as log]
    [webchange.common.handler :refer [handle current-user]]
    [webchange.progress.class-profile]
    [webchange.progress.student-profile]
    [webchange.validation.specs.class-spec :as class-spec]))

(defn handle-get-current-progress
  [course-slug request]
  (let [owner-id (current-user request)]
    (-> (core/get-current-progress course-slug owner-id)
        handle)))

(defn handle-save-course-progress
  [course-slug request]
  (let [owner-id (current-user request)
        data (-> request :body)]
    (-> (core/save-progress! owner-id course-slug data)
        handle)))

(defn handle-get-class-profile
  [class-id course-slug request]
  (let [user-id (current-user request)]
    (-> (core/get-class-profile course-slug (Integer/parseInt class-id))
        handle)))

(defn handle-get-individual-progress
  [student-id course-id request]
  (let [user-id (current-user request)]
    (-> (core/get-individual-progress (Integer/parseInt course-id) (Integer/parseInt student-id))
        handle)))

(defn handle-complete-progress
  [student-id course-slug request]
  (let [user-id (current-user request)
        data (-> request :body)]
    (handle [true (core/complete-individual-progress! course-slug (Integer/parseInt student-id) data)])))

(defn handle-get-class-students-progress
  [class-id request]
  (let [user-id (current-user request)]
    (-> (core/get-class-students-progress class-id)
        response)))

(defroutes progress-routes
  (GET "/api/class-profile/:class-id/course/:course-slug" [class-id course-slug :as request]
       (handle-get-class-profile class-id course-slug request))
  (GET "/api/individual-profile/:student-id/course/:course-slug" [student-id course-slug :as request]
       (handle-get-individual-progress student-id course-slug request))
  (GET "/api/courses/:course-slug/current-progress" [course-slug :as request]
       (handle-get-current-progress course-slug request))
  (POST "/api/courses/:course-slug/current-progress" [course-slug :as request]
        (handle-save-course-progress course-slug request))
  (PUT "/api/individual-profile/:student-id/course/:course-slug/complete" [student-id course-slug :as request]
       (handle-complete-progress student-id course-slug request))
  (GET "/api/class-students/:class-id/progress" request
       :coercion :spec
       :path-params [class-id :- ::class-spec/id]
       (handle-get-class-students-progress class-id request)))
