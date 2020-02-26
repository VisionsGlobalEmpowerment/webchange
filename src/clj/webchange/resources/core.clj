(ns webchange.resources.core
  (:require
    [webchange.resources.core-activities :as ca]
    [webchange.resources.core-game-app :as cga]
    [webchange.resources.core-web-app :as cwa]))

(defn- wrap
  [data]
  [true data])

(defn get-activities-resources
  [course-name]
  (-> (ca/get-activities-resources course-name)
      wrap))

(defn get-start-resources
  [course-name]
  (-> (ca/get-start-resources course-name)
      wrap))

(defn get-game-app-resources
  []
  (-> (cga/get-game-app-resources)
      wrap))

(defn get-web-app-resources
  []
  (-> (cwa/get-web-app-resources)
      wrap))
