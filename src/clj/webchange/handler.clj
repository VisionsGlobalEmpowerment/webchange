(ns webchange.handler
  (:require [compojure.core :refer [GET POST PUT DELETE defroutes routes]]
            [compojure.route :refer [resources not-found]]
            [ring.util.response :refer [resource-response response redirect]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [webchange.auth.core :refer [login! register-user! user-id-from-identity]]
            [webchange.course.core :as course]
            [webchange.dataset.core :as dataset]
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

(defn handle-create-dataset
  [request]
  (let [owner-id (current-user request)
        data (-> request :body)]
    (-> (dataset/create-dataset! data)
        handle)))

(defn handle-update-dataset
  [dataset-id request]
  (let [owner-id (current-user request)
        data (-> request :body)]
    (-> (dataset/update-dataset! (Integer/parseInt dataset-id) data)
        handle)))

(defn handle-create-dataset-item
  [request]
  (let [owner-id (current-user request)
        data (-> request :body)]
    (-> (dataset/create-dataset-item! data)
        handle)))

(defn handle-update-dataset-item
  [id request]
  (let [owner-id (current-user request)
        data (-> request :body)]
    (-> (dataset/update-dataset-item! (Integer/parseInt id) data)
        handle)))

(defn handle-delete-dataset-item
  [id request]
  (let [owner-id (current-user request)]
    (-> (dataset/delete-dataset-item! (Integer/parseInt id))
        handle)))

(defn handle-create-lesson-set
  [request]
  (let [owner-id (current-user request)
        data (-> request :body)]
    (-> (dataset/create-lesson-set! data)
        handle)))

(defn handle-update-lesson-set
  [id request]
  (let [owner-id (current-user request)
        data (-> request :body)]
    (-> (dataset/update-lesson-set! (Integer/parseInt id) data)
        handle)))

(defn handle-delete-lesson-set
  [id request]
  (let [owner-id (current-user request)]
    (-> (dataset/delete-lesson-set! (Integer/parseInt id))
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

           (GET "/api/datasets/:id" [id] (-> id Integer/parseInt dataset/get-dataset response))
           (GET "/api/courses/:course-id/datasets" [course-id] (-> course-id dataset/get-course-datasets response))
           (POST "/api/datasets" request
             (handle-create-dataset request))
           (PUT "/api/datasets/:id" [id :as request]
             (handle-update-dataset id request))

           (GET "/api/datasets/:id/items" [id] (-> id Integer/parseInt dataset/get-dataset-items response))
           (GET "/api/dataset-items/:id" [id]
             (if-let [item (-> id Integer/parseInt dataset/get-item)]
               (response {:item item})
               (not-found "not found")))
           (POST "/api/dataset-items" request
             (handle-create-dataset-item request))
           (PUT "/api/dataset-items/:id" [id :as request]
             (handle-update-dataset-item id request))
           (DELETE "/api/dataset-items/:id" [id :as request]
                   (handle-delete-dataset-item id request))

           (GET "/api/datasets/:id/lesson-sets" [id] (-> id Integer/parseInt dataset/get-dataset-lessons response))
           (GET "/api/lesson-sets/:name" [name]
             (if-let [item (-> name dataset/get-lesson-set-by-name)]
               (response {:lesson-set item})
               (not-found "not found")))
           (POST "/api/lesson-sets" request
             (handle-create-lesson-set request))
           (PUT "/api/lesson-sets/:id" [id :as request]
             (handle-update-lesson-set id request))
           (DELETE "/api/lesson-sets/:id" [id :as request]
             (handle-delete-lesson-set id request))
           )

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
