(ns webchange.handler
  (:require [compojure.core :refer [GET POST PUT DELETE defroutes routes]]
            [compojure.route :refer [resources not-found]]
            [ring.util.response :refer [resource-response response redirect]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [webchange.auth.core :refer [login! register-user! user-id-from-identity]]
            [webchange.course.core :as course]
            [webchange.class.handler :refer [class-routes]]
            [webchange.progress.handler :refer [progress-routes]]
            [webchange.dataset.handler :refer [dataset-routes]]
            [ring.middleware.session :refer [wrap-session]]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [buddy.auth.backends.session :refer [session-backend]]
            [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
            [clojure.tools.logging :as log]
            [ring.middleware.session.memory :as mem]
            [webchange.common.handler :refer [handle current-user]]))

(defn api-request? [request] (= "application/json" (:accept request)))

(defn unauthorized-handler
  [request metadata]
  (if (api-request? request)
    (if (authenticated? request)
      {:status 403
       :body {:errors [{:message "Unauthorized"}]}}
      {:status 401
       :body {:errors [{:message "Unauthenticated"}]}})
    (if (authenticated? request)
      (resource-response "error403.html" {:root "public"})
      (redirect "/login"))))

(def auth-backend
  (session-backend {:unauthorized-handler unauthorized-handler}))

(defn wrap-exception-handling
  [handler]
  (fn [request]
    (try
      (handler request)
      (catch Exception e
        (log/error e)
        {:status 400 :body (str "Invalid data" e)}))))

(defn with-updated-session
  [request]
  (fn [data response] (assoc response :session (merge (:session request) {:identity (-> data :email)}))))

(defn handle-save-scene
  [course-id scene-id request]
  (let [owner-id (current-user request)
        save (fn [data] (course/save-scene! course-id scene-id data owner-id))]
    (-> request
        :body
        :scene
        save
        handle)))

(defn handle-save-course
  [course-id request]
  (let [owner-id (current-user request)
        save (fn [data] (course/save-course! course-id data owner-id))]
    (-> request
        :body
        :course
        save
        handle)))

(defn handle-restore-course-version
  [version-id request]
  (let [owner-id (current-user request)]
    (-> (course/restore-course-version! (Integer/parseInt version-id) owner-id)
        handle)))

(defn handle-restore-scene-version
  [version-id request]
  (let [owner-id (current-user request)]
    (-> (course/restore-scene-version! (Integer/parseInt version-id) owner-id)
        handle)))

(defn public-route [] (resource-response "index.html" {:root "public"}))
(defn authenticated-route [request] (if-not (authenticated? request)
                         (throw-unauthorized)
                         (resource-response "index.html" {:root "public"})))

(defroutes pages-routes
           (GET "/" [] (public-route))
           (GET "/login" [] (public-route))
           (GET "/register" [] (public-route))

           (GET "/editor" request (authenticated-route request))
           (GET "/courses/:id" request (authenticated-route request))
           (GET "/courses/:id/editor" request (authenticated-route request))

           (GET "/dashboard" request (authenticated-route request))
           (resources "/"))

(defroutes api-routes
           (GET "/api/courses/:course-id" [course-id] (-> course-id course/get-course-data response))
           (GET "/api/courses/:course-id/scenes/:scene-id" [course-id scene-id] (-> (course/get-scene-data course-id scene-id) response))
           (POST "/api/courses/:course-id/scenes/:scene-id" [course-id scene-id :as request]
             (handle-save-scene course-id scene-id request))
           (POST "/api/courses/:course-id" [course-id :as request]
             (handle-save-course course-id request))
           (POST "/api/users/login" request
             (-> request :body :user login! (handle (with-updated-session request))))
           (POST "/api/users/register-user" request
             (-> request :body :user register-user! handle))

           (GET "/api/courses/:course-id/versions" [course-id] (-> course-id course/get-course-versions response))
           (GET "/api/courses/:course-id/scenes/:scene-id/versions" [course-id scene-id] (-> (course/get-scene-versions course-id scene-id) response))
           (POST "/api/course-versions/:version-id/restore" [version-id :as request]
             (handle-restore-course-version version-id request))
           (POST "/api/scene-versions/:version-id/restore" [version-id :as request]
             (handle-restore-scene-version version-id request))


           )

(defroutes app
           pages-routes
           api-routes
           class-routes
           dataset-routes
           progress-routes
           (not-found "Not Found"))

(def dev-store (mem/memory-store))

(def dev-handler (-> #'app
                     wrap-reload
                     (wrap-authorization auth-backend)
                     (wrap-authentication auth-backend)
                     (wrap-json-body {:keywords? true})
                     wrap-json-response
                     (wrap-session {:store dev-store})
                     wrap-exception-handling))

(def handler (-> #'app
                 (wrap-authorization auth-backend)
                 (wrap-authentication auth-backend)
                 (wrap-json-body {:keywords? true})
                 wrap-json-response
                 wrap-session
                 wrap-exception-handling))
