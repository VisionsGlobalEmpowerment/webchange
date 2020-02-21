(ns webchange.service-worker.config
  (:require
    [clojure.string :refer [join]]))

(def app-name "webchange")
(def release-number 4)

(def cache-names-prefix app-name)
(def database-name app-name)

(def api-path "/api/")
(def log-level :debug)

(defn get-cache-name
  [key course-name]
  (let [cache-names {:api    "api"
                     :static "static"
                     :game   "game"}]
    (join "-" [cache-names-prefix (get cache-names key) release-number course-name])))
