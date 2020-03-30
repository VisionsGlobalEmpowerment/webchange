(ns webchange.course.handler
  (:require [compojure.core :refer [GET POST PUT DELETE defroutes routes]]
            [compojure.route :refer [resources not-found]]
            [ring.util.response :refer [resource-response response redirect]]
            [webchange.course.core :as core]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [clojure.tools.logging :as log]
            [webchange.common.handler :refer [handle current-user current-school validation-error]]
            [webchange.validation.validate :refer [validate]]))

(defn handle-save-scene
  [course-slug scene-name request]
  (let [owner-id (current-user request)
        save (fn [data] (core/save-scene! course-slug scene-name data owner-id))]
    (-> request
        :body
        :scene
        save
        handle)))

(defn handle-save-course
  [course-slug request]
  (let [owner-id (current-user request)
        save (fn [data] (core/save-course! course-slug data owner-id))]
    (-> request
        :body
        :course
        save
        handle)))

(defn handle-save-course-info
  [course-id request]
  (let [owner-id (current-user request)
        save (fn [data] (core/save-course-info! (Integer/parseInt course-id) data))]
    (-> request
        :body
        save
        handle)))

(defn handle-restore-course-version
  [version-id request]
  (let [owner-id (current-user request)]
    (-> (core/restore-course-version! (Integer/parseInt version-id) owner-id)
        handle)))

(defn handle-restore-scene-version
  [version-id request]
  (let [owner-id (current-user request)]
    (-> (core/restore-scene-version! (Integer/parseInt version-id) owner-id)
        handle)))

(defroutes course-routes
           (GET "/api/courses/:course-slug" [course-slug] (-> course-slug core/get-course-data response))
           (GET "/api/courses/:course-slug/scenes/:scene-name" [course-slug scene-name] (-> (core/get-scene-data course-slug scene-name) response))
           (POST "/api/courses/:course-slug/scenes/:scene-name" [course-slug scene-name :as request]
             (handle-save-scene course-slug scene-name request))
           (POST "/api/courses/:course-slug" [course-slug :as request]
             (handle-save-course course-slug request))

           (GET "/api/courses/:course-slug/info" [course-slug]
             (-> course-slug core/get-course-info response))
           (PUT "/api/courses/:course-id/info" [course-id :as request]
             (handle-save-course-info course-id request))

           (GET "/api/courses/:course-slug/versions" [course-slug] (-> course-slug core/get-course-versions response))
           (GET "/api/courses/:course-slug/scenes/:scene-name/versions" [course-slug scene-name] (-> (core/get-scene-versions course-slug scene-name) response))
           (POST "/api/course-versions/:version-id/restore" [version-id :as request]
             (handle-restore-course-version version-id request))
           (POST "/api/scene-versions/:version-id/restore" [version-id :as request]
             (handle-restore-scene-version version-id request)))
