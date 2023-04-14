(ns webchange.handler
  (:require
    [buddy.auth :refer [authenticated? throw-unauthorized] :as ba]
    [buddy.auth.backends.session :refer [session-backend]]
    [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
    [clojure.edn :as edn]
    [clojure.tools.logging :as log]
    [compojure.api.exception :as ex]
    [compojure.api.sweet :refer [api defroutes GET POST swagger-routes]]
    [compojure.route :refer [files not-found resources]]
    [config.core :refer [env]]
    [muuntaja.middleware :as middleware]
    [ring.middleware.cookies :refer [wrap-cookies]]
    [ring.middleware.params :refer [wrap-params]]
    [ring.middleware.reload :refer [wrap-reload]]
    [ring.middleware.session :refer [wrap-session]]
    [ring.middleware.session.cookie :refer [cookie-store]]
    [ring.middleware.session.memory :as mem]
    [ring.util.response :refer [bad-request header redirect resource-response
                                response status] :as ring-response]
    [webchange.assets.handler :refer [asset-maintainer-routes asset-routes asset-api-routes]]
    [webchange.auth.handler :refer [auth-routes]]
    [webchange.auth.roles :as roles]
    [webchange.auth.website :as website]
    [webchange.accounts.handler :refer [accounts-routes accounts-pages-routes]]
    [webchange.book-library.handler :refer [book-library-api-routes]]
    [webchange.class.handler :refer [class-routes]]
    [webchange.common.audio-parser :refer [get-talking-animation]]
    [webchange.common.handler :refer [current-user handle]]
    [webchange.common.hmac-sha256 :as sign]
    [webchange.course.handler :refer [course-pages-routes course-routes
                                      courses-api-routes editor-api-routes
                                      website-api-routes]]
    [webchange.dataset.handler :refer [dataset-api-routes dataset-routes]]
    [webchange.parent.handler :refer [child-api-routes parent-api-routes]]
    [webchange.progress.handler :refer [progress-routes]]
    [webchange.resources.handler :refer [resources-routes]]
    [webchange.school.handler :refer [school-routes]]
    [webchange.secondary.handler :refer [global-sync-routes local-sync-routes]]
    [webchange.templates.handler :refer [templates-api-routes]]
    [webchange.validation.validate :refer [phrase-problems]]))

(defn api-request?
  [request]
  (-> request :headers (get "accept") (= "application/json")))

(defn unauthorized-handler
  [request _metadata]
  (if (api-request? request)
    (if (authenticated? request)
      {:status 403
       :body   {:errors [{:message "Unauthorized"}]}}
      {:status 401
       :body   {:errors [{:message "Unauthenticated"}]}})
    (if (authenticated? request)
      (-> (resource-response "error403.html" {:root "public"}) (status 403))
      (redirect "/accounts/login"))))

(def auth-backend
  (session-backend {:unauthorized-handler unauthorized-handler}))

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

(defn public-student-route [] (resource-response "index.html" {:root "public"}))

(defn student-route
  [request]
  (if-not (authenticated? request)
    (throw-unauthorized {:role :student})
    (resource-response "index.html" {:root "public"})))

(defn parent-route
  [request]
  (if-not (authenticated? request)
    (throw-unauthorized {:role :parent})
    (resource-response "index.html" {:root "public"})))

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
  (GET "/student-login/:school-id" [] (public-student-route))
  (GET "/register" [] (public-route))

  (GET "/s/:scene-id" [] (public-route))
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
  (GET "/courses/:id/book-library" request (student-route request))
  (GET "/courses/:id/book-library/favorite" request (student-route request))
  (GET "/courses/:id/book-library/search" request (student-route request))
  (GET "/courses/:id/book-library/read/:book-id" request (student-route request))

  ;; parent routes
  (GET "/parents" request (parent-route request))
  (GET "/parents/*" request (parent-route request))

  ;; Wizard
  (GET "/game-changer" request (authenticated-route request {:role :educator}))
  (GET "/game-changer/:course-slug/:scene-slug" request (authenticated-route request {:role :educator}))
  (GET "/book-creator" request (authenticated-route request {:role :educator}))
  (GET "/lesson-builder/:scene-id" request (authenticated-route request {:role :educator}))
  (GET "/wizard" request (authenticated-route request {:role :educator}))
  (GET "/game-changer-beta" request (authenticated-route request {:role :educator}))

  (GET "/educators" [] (public-route))
  (GET "/educators/*" [] (public-route))
  (GET "/teacher" [] (public-route))
  (GET "/teacher/*" [] (public-route))
  (GET "/ui" [] (public-route))
  (GET "/ui/*" [] (public-route))
  (GET "/accounts/*" [] (public-route))
  
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

(defn handle-save-log
  [request]
  (let [user (-> request :session :identity :id)
        body (:body request)
        type (:type body)]
    (log/info "system-log - user:" user "type:" type "body:" body)
    (response {:message "ok"})))

(defroutes system-routes
           (POST "/api/system/log" request
                (handle-save-log request)))

(defn wrap-body-as-string
  [handler]
  (fn [{body-params :body-params :as request}]
    (let [response (if body-params
                     (handler (assoc request :body body-params))
                     (handler request))]
      response)))

(defn wrap-log
  [handler]
  (fn [request]
    #_(log/debug "Request: " (str "Uri: " (:uri request) " " (:request-method
                                                              request)))
    (handler request)))

(defn request-validation-handler
  [e data req]
  (if-let [problems (-> data :problems :clojure.spec.alpha/problems)]
    (bad-request {:errors (phrase-problems problems)})
    (bad-request {:errors (:errors data)})))

(defn request-auth-handler
  [e data req]
  (if (= (::ba/type data) ::ba/unauthorized)
    (unauthorized-handler req data)
    (ex/safe-handler e data req)))

(defn request-not-found
  [e data req]
  (ring-response/not-found {:errors (:errors data)}))

(defroutes app
  (api
   :exceptions {:handlers {::ex/request-validation request-validation-handler
                           ::ex/default request-auth-handler
                           ::ex/not-found request-not-found}}
   (swagger-routes {:ui   "/api-docs"
                    :data {:info {:title "TabSchools API"}
                           :tags [{:name "dataset", :description "Dataset APIs"}
                                  {:name "course", :description "Courses APIs"}]}})
   website-api-routes
   editor-api-routes
   courses-api-routes
   dataset-api-routes
   templates-api-routes
   class-routes
   accounts-routes
   course-routes
   accounts-pages-routes
   asset-api-routes)
  pages-routes
  animation-routes
  auth-routes
  course-pages-routes
  
  school-routes
  global-sync-routes
  local-sync-routes
  asset-maintainer-routes
  book-library-api-routes
  dataset-routes
  progress-routes
  child-api-routes
  parent-api-routes
  resources-routes
  service-worker-route
  asset-routes
  system-routes
  (not-found "Not Found"))

(def dev-store (mem/memory-store))
(def store (cookie-store {:key (env :session-key)}))


(def handler
  (-> #'app
      (wrap-authorization auth-backend)
      (wrap-authentication auth-backend)
      (wrap-session {:store store})
      wrap-cookies
      wrap-body-as-string
      (middleware/wrap-params)
      (middleware/wrap-format)
      (middleware/wrap-exception)
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
      wrap-log
      (middleware/wrap-params)
      (middleware/wrap-format)
      (middleware/wrap-exception)
      (sign/wrap-body-as-byte-array)))
