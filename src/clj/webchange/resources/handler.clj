(ns webchange.resources.handler
  (:require [compojure.core :refer [GET defroutes]]
            [webchange.resources.core :as core]
            [webchange.common.handler :refer [handle]]))

(defn handle-app-resources
  []
  (-> (core/get-app-resources)
      handle))

(defn handle-level-resources
  [_]
  (-> (core/get-level-resources)
      handle))

(defroutes resources-routes
           (GET "/api/resources/app" _ (handle-app-resources))
           (GET "/api/resources/level/:level-id" [level-id] (handle-level-resources level-id)))
