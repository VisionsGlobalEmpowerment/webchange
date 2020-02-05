(ns webchange.service-worker.common.fetch
  (:require
    [webchange.service-worker.wrappers :refer [then js-fetch response-json]]))

(defn- get-resources-from-api
  [url]
  (-> (js-fetch url)
      (then response-json)))

(defn fetch-web-app-resources
  []
  (get-resources-from-api "/api/resources/student-dashboard"))

(defn fetch-game-app-resources
  []
  (get-resources-from-api "/api/resources/game-app"))

(defn fetch-game-start-resources
  [course-name]
  (get-resources-from-api (str "/api/resources/game-app/" course-name "/start-resources")))
