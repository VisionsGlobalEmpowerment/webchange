(ns webchange.resources.handler
  (:require
    [compojure.core :refer [GET defroutes]]
    [webchange.common.handler :refer [handle]]
    [webchange.resources.core-web-app :as web-app]))

(defn- wrap
  [data]
  (handle [true data]))

(defn handle-web-app-resources
  []
  (-> (web-app/get-web-app-resources)
      (wrap)))

(defroutes resources-routes
           (GET "/api/resources/web-app" _ (handle-web-app-resources)))
