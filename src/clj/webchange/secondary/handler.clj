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
            [webchange.validation.specs.student :as student-specs]))

(defn handle-dump-full [id request]
      (response (core/get-dump-by-school id)))

(defroutes secondary-school-routes
           (GET "/api/school/dump-full/:id" [id :as request] (handle-dump-full id request)))