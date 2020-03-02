(ns webchange.handler
  (:require [compojure.core :refer [GET POST PUT DELETE defroutes routes]]
            [compojure.route :refer [resources files not-found]]
            [ring.util.response :refer [resource-response response redirect]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]
            [webchange.auth.handler :refer [auth-routes]]
            [webchange.common.audio-parser :refer [get-talking-animation]]
            [webchange.course.core :as course]
            [webchange.class.handler :refer [class-routes]]
            [webchange.progress.handler :refer [progress-routes]]
            [webchange.dataset.handler :refer [dataset-routes]]
            [webchange.assets.handler :refer [asset-routes]]
            [webchange.resources.handler :refer [resources-routes]]
            [ring.middleware.session :refer [wrap-session]]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [buddy.auth.backends.session :refer [session-backend]]
            [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
            [clojure.edn :as edn]
            [clojure.tools.logging :as log]
            [ring.middleware.session.memory :as mem]
            [webchange.common.handler :refer [handle current-user]]
            [config.core :refer [env]]))

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
      (redirect (str "/login" "?redirect=" (:uri request))))))

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

(defn handle-parse-audio-animation
  [request]
  (let [{:strs [file start duration]} (:query-params request)]
    (-> [true (get-talking-animation file
                                     (edn/read-string start)
                                     (edn/read-string duration))]
        handle)))

(defn public-route [] (resource-response "index.html" {:root "public"}))
(defn authenticated-route [request] (if-not (authenticated? request)
                         (throw-unauthorized)
                         (resource-response "index.html" {:root "public"})))

(defn teacher? [request]
  (and (authenticated? request)
       (-> request :session :identity :teacher-id)))

(defn teachers-route [request]
  (if-not (teacher? request)
    (throw-unauthorized)
    (resource-response "index.html" {:root "public"})))

(defroutes pages-routes
           (GET "/" [] (public-route))
           (GET "/login" [] (public-route))
           (GET "/student-login" [] (public-route))
           (GET "/register" [] (public-route))

           (GET "/courses/:id" request (authenticated-route request))
           (GET "/courses/:id/editor" request (authenticated-route request))
           (GET "/courses/:id/editor-v2" request (authenticated-route request))
           (GET "/courses/:id/editor-v2/:scene-id" request (authenticated-route request))
           (GET "/courses/:id/editor-v2/concepts/:concept-id" request (authenticated-route request))
           (GET "/courses/:id/editor-v2/add-concept" request (authenticated-route request))

           (GET "/dashboard" request (teachers-route request))
           (GET "/dashboard/classes" request (teachers-route request))
           (GET "/dashboard/classes/:class-id" request (teachers-route request))
           (GET "/dashboard/classes/:class-id/students" request (teachers-route request))
           (GET "/dashboard/classes/:class-id/students/:student-id" request (teachers-route request))

           (GET "/student-dashboard" request (authenticated-route request))
           (GET "/courses/:id/dashboard" request (authenticated-route request))
           (GET "/student-dashboard/finished" request (authenticated-route request))
           (GET "/courses/:id/dashboard/finished" request (authenticated-route request))
           (files "/upload/" {:root (env :upload-dir)})
           (resources "/"))

(defroutes api-routes
           (GET "/api/courses/:course-id" [course-id] (-> course-id course/get-course-data response))
           (GET "/api/courses/:course-id/scenes/:scene-id" [course-id scene-id] (-> (course/get-scene-data course-id scene-id) response))
           (POST "/api/courses/:course-id/scenes/:scene-id" [course-id scene-id :as request]
             (handle-save-scene course-id scene-id request))
           (POST "/api/courses/:course-id" [course-id :as request]
             (handle-save-course course-id request))

           (GET "/api/courses/:course-id/versions" [course-id] (-> course-id course/get-course-versions response))
           (GET "/api/courses/:course-id/scenes/:scene-id/versions" [course-id scene-id] (-> (course/get-scene-versions course-id scene-id) response))
           (POST "/api/course-versions/:version-id/restore" [version-id :as request]
             (handle-restore-course-version version-id request))
           (POST "/api/scene-versions/:version-id/restore" [version-id :as request]
             (handle-restore-scene-version version-id request))

           (GET "/api/actions/get-talk-animations" _ (->> handle-parse-audio-animation wrap-params)))

(defroutes service-worker-route
           (GET "/page-skeleton" [] (public-route))
           (GET "/service-worker.js" [] (-> (resource-response "js/compiled/service-worker.js" {:root "public"})
                                            (assoc-in [:headers "Content-Type"] "text/javascript"))))

(defroutes app
           pages-routes
           api-routes
           auth-routes
           class-routes
           dataset-routes
           progress-routes
           resources-routes
           service-worker-route
           (-> asset-routes
               wrap-multipart-params)
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
