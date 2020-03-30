(ns webchange.resources.handler
  (:require
    [compojure.core :refer [GET defroutes]]
    [webchange.common.handler :refer [handle]]
    [webchange.resources.core :as core]))

(defn handle-activities-resources
  [course-slug]
  (-> (core/get-activities-resources course-slug)
      handle))

(defn handle-start-resources
  [course-slug]
  (-> (core/get-start-resources course-slug)
      handle))

(defn handle-game-app-resources
  []
  (-> (core/get-game-app-resources)
      handle))

(defn handle-web-app-resources
  []
  (-> (core/get-web-app-resources)
      handle))

(defroutes resources-routes
           (GET "/api/resources/student-dashboard" _ (handle-web-app-resources))
           (GET "/api/resources/game-app" _ (handle-game-app-resources))
           (GET "/api/resources/game-app/:course-slug/scenes" [course-slug] (handle-activities-resources course-slug))
           (GET "/api/resources/game-app/:course-slug/start-resources" [course-slug] (handle-start-resources course-slug)))
