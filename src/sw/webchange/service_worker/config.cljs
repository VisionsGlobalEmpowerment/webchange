(ns webchange.service-worker.config
  (:require
    [clojure.string :refer [join]]))

(defn- time-stamp
  []
  (let [date (js/Date.)]
    (join "-" [(.getFullYear date)
               (+ 1 (.getMonth date))
               (.getDate date)
               (.getHours date)
               (.getMinutes date)
               (.getSeconds date)])))

(def app-name "webchange")
(def release-number (time-stamp))

(def cache-names-prefix app-name)
(def database-name app-name)

(defn- storage-name
  [name]
  (join "-" [cache-names-prefix name release-number]))

(def api-path "/api/")

(def cache-names {:api    (storage-name "api")
                  :static (storage-name "static")
                  :game   (storage-name "game")})

(def log-level :log)
