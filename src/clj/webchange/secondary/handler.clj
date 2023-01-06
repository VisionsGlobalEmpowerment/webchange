(ns webchange.secondary.handler
  (:require [compojure.core :refer [GET POST PUT DELETE defroutes routes]]
            [ring.util.response :refer [file-response bad-request response]]
            [webchange.secondary.core :as core]
            [webchange.common.handler :refer [current-user]]
            [webchange.auth.roles :refer [is-admin?]]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [clojure.tools.logging :as log]
            [webchange.db.core :refer [*db*] :as db]
            [clojure.data.json :as json]
            [config.core :refer [env]]
            [webchange.secondary.updater :as updater]
            [webchange.school.core :as school]
            [webchange.common.hmac-sha256 :as sign]))

(defn teacher? [request]
  (and (authenticated? request)
       (-> request :session :identity :teacher-id)))

(defn handle-dump-full [id request]
  (response (core/get-dump-by-school id)))

(defn handle-get-school-update [id request]
  (let [requested-courses (-> request :body :requested-courses)]
    (response (core/get-course-update id requested-courses))))

(defn handle-load-school-update [id request]
  (let [data (-> request :body slurp (json/read-str :key-fn keyword))]
    (core/import-secondary-data! (Integer/parseInt id) data)
    (response {})))

(defn handle-load-school-sync
  [id request]
  (if (env :secondary)
    (let [school-id (Integer/parseInt id)
          owner-id (current-user request)]
      (when-not (or (is-admin? owner-id) (school/school-admin? school-id owner-id))
        (throw-unauthorized {:role :educator}))
      (updater/start-sync! school-id)
      (response {:result "Ok"}))
    (bad-request "not a secondary school")))

(defn handle-asset-difference
  [request]
  (let [school-id (-> request :body :school-id)
        school-hashes (-> request :body :hashes)
        requested-courses (-> request :body :course-slugs)]
    (response (core/calc-asset-update school-id school-hashes requested-courses))))

(defn handle-get-latest-version
  []
  (let [file-path (env :latest-binary-path)]
    (if (nil? file-path)
      (bad-request "no update available")
      (file-response file-path))))

(defn handle-software-update
  [request]
  (when-not (teacher? request)
    (throw-unauthorized {:role :teacher}))
  (if (env :secondary)
    (updater/update-local-instance!)
    (bad-request "not a secondary school")))

(defn handle-sync-status
  [request]
  (-> (updater/get-sync-status)
      (response)))

(defroutes local-sync-routes
  (POST "/api/school/sync/:id" [id :as request] (handle-load-school-sync id request))
  (GET "/api/school/sync-status" request (handle-sync-status request))
  (POST "/api/software/update" request (handle-software-update request)))

(defroutes global-sync-routes
  (GET "/api/school/dump-full/:id" [id :as request] (handle-dump-full id request))
  (PUT "/api/school/update/:id" [id :as request] (handle-load-school-update id request))
  (GET "/api/school/courses-update/:id" [id :as request] (handle-get-school-update id request))
  (POST "/api/school/courses-update/:id" [id :as request] (handle-get-school-update id request))
  (POST "/api/school/asset/difference/" request (handle-asset-difference request))
  (sign/wrap-api-with-signature
    (GET "/api/software/latest" _ (handle-get-latest-version))))

