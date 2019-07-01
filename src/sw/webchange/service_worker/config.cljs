(ns webchange.service-worker.config
  (:require
    [clojure.string :refer [join]]))

(def release-number 1)
(def cache-names-prefix "webchange")
(def cache-names {:static (join "-" [cache-names-prefix "static" release-number])})
