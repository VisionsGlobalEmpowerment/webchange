(ns webchange.handler
  (:require [compojure.core :refer [GET POST defroutes routes]]
            [compojure.route :refer [resources not-found]]
            [ring.util.response :refer [resource-response response redirect]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [webchange.auth.core :refer [login! register-user! user-id-from-identity]]
            [webchange.course.core :refer [get-course-data get-scene-data save-scene!]]
            [ring.middleware.session :refer [wrap-session]]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [buddy.auth.backends.session :refer [session-backend]]
            [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
            [clojure.tools.logging :as log]
            [ring.middleware.session.memory :as mem]))

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

(defn with-updated-session
  [request]
  (fn [data response] (assoc response :session (merge (:session request) {:identity (-> data :email)}))))

(defn current-user
  [request]
  (if-not (authenticated? request)
    (throw-unauthorized)
    (-> request :session :identity user-id-from-identity)))

(defn handle-save-scene
  [course-id scene-id request]
  (let [owner-id (current-user request)
        save (fn [scene-data] (save-scene! course-id scene-id scene-data owner-id))]
    (-> request
        :body
        :scene
        save
        handle)))

(defroutes pages-routes
           (GET "/" [] (resource-response "index.html" {:root "public"}))
           (GET "/editor" request
             (if-not (authenticated? request)
               (throw-unauthorized)
               (resource-response "editor.html" {:root "public"})))
           (GET "/login" [] (resource-response "login.html" {:root "public"}))
           (resources "/"))

(defroutes api-routes
           (GET "/api/courses/:course-id" [course-id] (-> course-id get-course-data response))
           (GET "/api/courses/:course-id/scenes/:scene-id" [course-id scene-id] (-> (get-scene-data course-id scene-id) response))
           (POST "/api/courses/:course-id/scenes/:scene-id" [course-id scene-id :as request]
             (handle-save-scene course-id scene-id request))
           (POST "/api/users/login" request
             (-> request :body :user login! (handle (with-updated-session request))))
           (POST "/api/users/register-user" request
             (-> request :body :user register-user! handle)))

(defroutes app
           pages-routes
           api-routes
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
