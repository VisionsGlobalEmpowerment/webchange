(ns webchange.service-worker.config
  (:require
    [clojure.string :refer [join]]))

(def release-number 2)
(def cache-names-prefix "webchange")

(defn storage-name
  [name]
  (join "-" [cache-names-prefix name release-number]))

(def cache-names {:api    (storage-name "api")
                  :static (storage-name "static")})
