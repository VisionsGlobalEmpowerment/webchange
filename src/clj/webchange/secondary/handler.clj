(ns webchange.secondary.handler
  (:require [compojure.core :refer [GET POST PUT DELETE defroutes routes]]
            [ring.util.response :refer [file-response bad-request response]]
            [webchange.secondary.core :as core]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [clojure.tools.logging :as log]
            [webchange.db.core :refer [*db*] :as db]
            [clojure.data.json :as json]
            [config.core :refer [env]]
            [webchange.secondary.updater :as updater]
            [webchange.common.hmac-sha256 :as sign]))

(defn teacher? [request]
  (and (authenticated? request)
       (-> request :session :identity :teacher-id)))

(defn handle-dump-full [id request]
  (response (core/get-dump-by-school id)))

(defn handle-get-school-update [id request]
  (response (core/get-course-update id)))

(defn handle-load-school-update [id request]
  (let [data (-> request :body slurp (json/read-str :key-fn keyword))]
    (core/import-secondary-data! (Integer/parseInt id) data)
    response {}))

(defn handle-load-school-sync [id request]
  (let [school (db/get-school {:id (Integer/parseInt id)})]
    (when-not (teacher? request)
      (throw-unauthorized {:role :teacher}))
    (core/upload-stat (:id school))
    (core/update-course-data! (:id school))
    (core/update-assets!)
    (response {:result "Ok"})))

(defn handle-asset-difference [request]
  (let [school-hashes (-> request :body)]
    (response (core/calc-asset-update school-hashes))))

(defn handle-software-update
  [request]
  (when-not (teacher? request)
    (throw-unauthorized {:role :teacher}))
  (if (env :secondary)
    (updater/update-local-instance!)
    (bad-request "not a secondary school")))

(defn handle-get-latest-version
  []
  (let [file-path (env :latest-binary-path)]
    (if (nil? file-path)
      (bad-request "no update available")
      (file-response file-path))))

(defroutes secondary-school-routes
           (GET "/api/school/dump-full/:id" [id :as request] (handle-dump-full id request))
           (GET "/api/school/update/:id" [id :as request] (handle-get-school-update id request))
           (PUT "/api/school/update/:id" [id :as request] (handle-load-school-update id request))
           (POST "/api/school/sync/:id" [id :as request] (handle-load-school-sync id request))
           (POST "/api/school/asset/difference/" request (handle-asset-difference request))
           (POST "/api/software/update" request (handle-software-update request))
           (sign/wrap-api-with-signature
             (GET "/api/software/latest" _ (handle-get-latest-version))))
