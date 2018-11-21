(ns webchange.handler
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [resources]]
            [ring.util.response :refer [resource-response response]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.json :refer [wrap-json-response]]
            [webchange.scene :refer :all]))

(defroutes routes
           (GET "/" [] (resource-response "index.html" {:root "public"}))
           (GET "/api/courses/:course-id" [course-id] (-> course-id get-course response))
           (GET "/api/courses/:course-id/scenes/:scene-id" [course-id scene-id] (-> (get-scene course-id scene-id) response))
           (resources "/"))

(def dev-handler (-> #'routes wrap-reload wrap-json-response))

(def handler (-> #'routes wrap-json-response))
