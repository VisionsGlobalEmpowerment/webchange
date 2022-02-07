(ns webchange.handler
  (:require [compojure.api.sweet :refer [api context ANY GET POST PUT DELETE PATCH defroutes routes swagger-routes]]
            [compojure.route :refer [resources files not-found]]
            [ring.util.response :refer [resource-response response redirect status header]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]
            [ring.middleware.cookies :refer [wrap-cookies]]
            [webchange.auth.handler :refer [auth-routes]]
            [webchange.common.audio-parser :refer [get-talking-animation]]
            [webchange.course.handler :refer [course-pages-routes course-routes website-api-routes editor-api-routes courses-api-routes]]
            [webchange.class.handler :refer [class-routes]]
            [webchange.school.handler :refer [school-routes]]
            [webchange.secondary.handler :refer [local-sync-routes global-sync-routes]]
            [webchange.progress.handler :refer [progress-routes]]
            [webchange.parent.handler :refer [child-api-routes parent-api-routes]]
            [webchange.dataset.handler :refer [dataset-routes dataset-api-routes]]
            [webchange.assets.handler :refer [asset-routes asset-maintainer-routes]]
            [webchange.resources.handler :refer [resources-routes]]
            [webchange.templates.handler :refer [templates-api-routes]]
            [ring.middleware.session :refer [wrap-session]]
            [buddy.auth :refer [throw-unauthorized authenticated?]]
            [buddy.auth.backends.session :refer [session-backend]]
            [buddy.auth.middleware :refer [wrap-authentication wrap-authorization authorization-error]]
            [clojure.edn :as edn]
            [webchange.common.hmac-sha256 :as sign]
            [clojure.tools.logging :as log]
            [clojure.data.json :as json]
            [webchange.common.hmac-sha256 :as sign]
            [ring.middleware.session.memory :as mem]
            [webchange.common.handler :refer [handle current-user]]
            [config.core :refer [env]]
            [webchange.auth.website :as website]
            [webchange.auth.roles :as roles])
  )

(defn api-request? [request] (= "application/json" (:accept request)))

(defn- login-resource
  [{role :role} prev cookies]
  (case role
    :student (if (-> cookies (get "parent-login") :value (= "true"))
               (str (website/website-logout-page))
               (str "/student-login"))
    :teacher (str "/login" "?redirect=" prev)
    (str (website/website-logout-page) "?redirect=" prev)))

(defn unauthorized-handler
  [request metadata]
  (if (api-request? request)
    (if (authenticated? request)
      {:status 403
       :body   {:errors [{:message "Unauthorized"}]}}
      {:status 401
       :body   {:errors [{:message "Unauthenticated"}]}})
    (if (authenticated? request)
      (-> (resource-response "error403.html" {:root "public"}) (status 403))
      (redirect (login-resource metadata (:uri request) (:cookies request))))))

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

(defn handle-parse-audio-animation
  [request]
  (let [{:strs [file start duration]} (:query-params request)]
    (-> [true (get-talking-animation file
                                     (edn/read-string start)
                                     (edn/read-string duration))]
        handle)))

(defn public-route [] (resource-response "index.html" {:root "public"}))

(defn authenticated-route
  ([request]
   (authenticated-route request {}))
  ([request metadata]
   (if-not (authenticated? request)
     (throw-unauthorized metadata)
     (resource-response "index.html" {:root "public"}))))

(defn public-student-route [] (resource-response "student.html" {:root "public"}))

(defn student-route
  [request]
  (if-not (authenticated? request)
    (throw-unauthorized {:role :student})
    (resource-response "student.html" {:root "public"})))

(defn parent-route
  [request]
  (if-not (authenticated? request)
    (throw-unauthorized {:role :parent})
    (resource-response "student.html" {:root "public"})))

(defn teacher? [request]
  (and (authenticated? request)
       (-> request :session :identity :teacher-id)))

(defn teachers-route [request]
  (if-not (teacher? request)
    (throw-unauthorized {:role :teacher})
    (resource-response "index.html" {:root "public"})))

(defn- admin? [request]
  (let [current-user (current-user request)]
    (roles/is-admin? current-user)))

(defn admin-route [request]
  (if-not (admin? request)
    (throw-unauthorized {:role :teacher})
    (resource-response "index.html" {:root "public"})))

(defroutes pages-routes
           (GET "/" [] (public-route))
           (GET "/login" [] (public-route))
           (GET "/student-login" [] (public-student-route))
           (GET "/register" [] (public-route))

           (GET "/s/:course-id/:scene-id" [] (public-route))
           (GET "/s/:course-id/:scene-id/:encoded-items" [] (public-route))

           ;; admin routes
           (GET "/dashboard/courses" request (admin-route request))

           ;; teacher routes
           (GET "/dashboard" request (teachers-route request))
           (GET "/dashboard/classes" request (teachers-route request))
           (GET "/dashboard/schools" request (teachers-route request))
           (GET "/dashboard/classes/:class-id" request (teachers-route request))
           (GET "/dashboard/classes/:class-id/students" request (teachers-route request))
           (GET "/dashboard/classes/:class-id/students/:student-id" request (teachers-route request))

           ;; student routes
           (GET "/courses/:id" request (student-route request))
           (GET "/courses/:id/dashboard" request (student-route request))
           (GET "/courses/:id/dashboard/finished" request (student-route request))

           ;; parent routes
           (GET "/parents" request (parent-route request))
           (GET "/parents/add-student" request (parent-route request))
           (GET "/parents/help" request (parent-route request))

           ;; Wizard
           (GET "/game-changer" request (authenticated-route request {:role :educator}))
           (GET "/game-changer/:course-slug/:scene-slug" request (authenticated-route request {:role :educator}))
           (GET "/book-creator" request (authenticated-route request {:role :educator}))
           (GET "/wizard" request (authenticated-route request {:role :educator}))
           (GET "/game-changer-beta" request (authenticated-route request {:role :educator}))

           ;; Technical
           (GET "/test-ui" [] (public-route))

           (files "/upload/" {:root (env :upload-dir)})
           (resources "/"))

(defroutes animation-routes
           (GET "/api/actions/get-talk-animations" _ (->> handle-parse-audio-animation wrap-params)))

(defroutes service-worker-route
           (GET "/page-skeleton" [] (public-student-route))
           (GET "/service-worker.js" [] (-> (resource-response "js/compiled/service-worker.js" {:root "public"})
                                            (assoc-in [:headers "Content-Type"] "text/javascript")))
           (GET "/service-worker.js.map" [] (-> (resource-response "js/compiled/service-worker.js.map" {:root "public"})
                                                (assoc-in [:headers "Content-Type"] "application/json"))))

(defn wrap-body-as-string
  [handler]
  (fn [{body-params :body-params :as request}]
    (let [response (if body-params
                     (handler (assoc request :body body-params))
                     (handler request))]
      response)))

(defn- json-response
  [{body :body :as response}]
  (if (or (string? body) (nil? body))
    response
    (assoc response :body (slurp body))))

(defroutes app
  (api
             (swagger-routes {:ui   "/api-docs"
                              :data {:info {:title "TabSchools API"}
                                     :tags [{:name "dataset", :description "Dataset APIs"}
                                            {:name "course", :description "Courses APIs"}]}})
               website-api-routes
               editor-api-routes
               courses-api-routes
               dataset-api-routes
               templates-api-routes)
  pages-routes
  animation-routes
  auth-routes
  course-pages-routes
  course-routes
  class-routes
  school-routes
  global-sync-routes
  local-sync-routes
  asset-maintainer-routes
  dataset-routes
  progress-routes
  child-api-routes
  parent-api-routes
  resources-routes
  service-worker-route
  asset-routes
  (not-found "Not Found"))

(def dev-store (mem/memory-store))


(def handler
  (-> #'app
      (wrap-authorization auth-backend)
      (wrap-authentication auth-backend)
      (wrap-session {:store dev-store})
      wrap-cookies
      wrap-body-as-string
      (muuntaja.middleware/wrap-params)
      (muuntaja.middleware/wrap-format)
      (muuntaja.middleware/wrap-exception)
      (sign/wrap-body-as-byte-array)))

(defn wrap-connection-close
  [handler]
  (fn [request]
    (some-> (handler request) (header "Connection" "close"))))

(def dev-handler
  (-> #'app
      wrap-reload
      (wrap-authorization auth-backend)
      (wrap-authentication auth-backend)
      (wrap-session {:store dev-store})
      wrap-cookies
      wrap-body-as-string
      wrap-connection-close
      (muuntaja.middleware/wrap-params)
      (muuntaja.middleware/wrap-format)
      (muuntaja.middleware/wrap-exception)
      (sign/wrap-body-as-byte-array)))
