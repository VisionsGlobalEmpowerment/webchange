(ns webchange.service-worker.config
  (:require
    [clojure.string :refer [join]]))

(def app-name "webchange")
(def release-number 1)

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
