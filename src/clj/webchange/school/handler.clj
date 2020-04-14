(ns webchange.school.handler
  (:require [compojure.core :refer [GET POST PUT DELETE defroutes routes]]
            [compojure.route :refer [resources not-found]]
            [ring.util.response :refer [resource-response response redirect]]
            [webchange.school.core :as core]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [clojure.tools.logging :as log]
            [webchange.common.handler :refer [handle current-user current-school validation-error]]
            [webchange.auth.core :as auth]
            [webchange.validation.validate :refer [validate]]
            [webchange.validation.specs.student :as student-specs]))


(defn handle-current-school [request]
  (-> (core/get-current-school)
      response))

(defn handle-create-school
  [request]
  (let [owner-id (current-user request)
        data (-> request
                 :body
                 )]
    (-> (core/create-school! data)
        handle)))

(defn handle-list-schools [request]
    (-> (core/get-schools)
        response))

(defn handle-update-school
  [id request]
  (let [owner-id (current-user request)
        data (-> request :body)]
    (-> (core/update-school! (Integer/parseInt id) data)
        handle)))

(defn handle-delete-school
  [id request]
  (let [owner-id (current-user request)]
    (-> (core/delete-school! (Integer/parseInt id))
        handle)))

(defroutes school-routes
           (GET "/api/schools/current" request (handle-current-school request))
           (POST "/api/schools" request (handle-create-school request))
           (GET "/api/schools/:id" [id]
             (if-let [item (-> id Integer/parseInt core/get-school)]
               (response {:school item})
               (not-found "not found")))
           (GET "/api/schools" request (handle-list-schools request))
           (PUT "/api/schools/:id" [id :as request]
             (handle-update-school id request))
           (DELETE "/api/schools/:id" [id :as request]
             (handle-delete-school id request))

           )