(ns webchange.secondary.handler
  (:require [compojure.core :refer [GET POST PUT DELETE defroutes routes]]
            [compojure.route :refer [resources not-found]]
            [ring.util.response :refer [resource-response response redirect]]
            [webchange.secondary.core :as core]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [clojure.tools.logging :as log]
            [webchange.common.handler :refer [handle current-user current-school validation-error]]
            [webchange.auth.core :as auth]
            [webchange.db.core :refer [*db*] :as db]
            [webchange.validation.validate :refer [validate]]
            [clojure.data.json :as json]
            [webchange.common.hmac-sha256 :as sign]
            [webchange.validation.specs.student :as student-specs]
            [webchange.common.hmac-sha256 :as sign]))

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
    (core/upload-stat (:id school))
    (core/update-course-data! (:id school))
    (core/update-assets!)
    (response {:hello "Ok"})))

(defn handle-asset-difference [request]
  (let [school-hashes (-> request :body)]
    (response (core/calc-asset-update school-hashes))))

(defroutes secondary-school-routes
           (GET "/api/school/dump-full/:id" [id :as request] (handle-dump-full id request))
           (GET "/api/school/update/:id" [id :as request] (handle-get-school-update id request))
           (PUT "/api/school/update/:id" [id :as request] (handle-load-school-update id request))
           (POST "/api/school/sync/:id" [id :as request] (handle-load-school-sync id request))
           (POST "/api/school/asset/difference/" request (handle-asset-difference request)))