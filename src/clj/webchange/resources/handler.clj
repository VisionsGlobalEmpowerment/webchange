(ns webchange.resources.handler
  (:require
    [compojure.core :refer [GET defroutes]]
    [webchange.common.handler :refer [handle]]
    [webchange.resources.core-activities :refer [get-activities-resources]]
    [webchange.resources.core-web-app :refer [get-web-app-resources]]))

(defn- wrap
  [data]
  (handle [true data]))

(defn handle-activities-resources
  [course-slug]
  (-> (get-activities-resources course-slug)
      (wrap)))

(defn handle-web-app-resources
  []
  (-> (get-web-app-resources)
      (wrap)))

(defroutes resources-routes
           (GET "/api/resources/student-dashboard" _ (handle-web-app-resources))
           (GET "/api/resources/game-app/:course-slug/scenes" [course-slug] (handle-activities-resources course-slug)))
