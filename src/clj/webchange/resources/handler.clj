(ns webchange.resources.handler
  (:require
    [compojure.core :refer [GET defroutes]]
    [webchange.common.handler :refer [handle]]
    [webchange.resources.core :as core]))

(defn handle-activities-resources
  [course-name]
  (-> (core/get-activities-resources course-name)
      handle))

(defn handle-start-resources
  [course-name]
  (-> (core/get-start-resources course-name)
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
           (GET "/api/resources/game-app/:course-name/scenes" [course-name] (handle-activities-resources course-name))
           (GET "/api/resources/game-app/:course-name/start-resources" [course-name] (handle-start-resources course-name)))
