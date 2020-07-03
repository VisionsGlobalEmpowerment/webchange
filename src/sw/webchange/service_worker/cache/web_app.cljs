(ns webchange.service-worker.cache.web-app
  (:require
    [webchange.service-worker.cache.core :as core]
    [webchange.service-worker.wrappers :refer [then]]))

(defn cache-resources
  [resources]
  (-> (core/get-cache-name :static)
      (then (fn [cache-name]
              (core/cache-resources cache-name resources)))))
