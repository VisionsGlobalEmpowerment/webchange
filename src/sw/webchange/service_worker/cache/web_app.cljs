(ns webchange.service-worker.cache.web-app
  (:require
    [webchange.service-worker.cache.core :as core]
    [webchange.service-worker.wrappers :refer [then]]))

(defn cache-resources
  ([resources]
   (cache-resources resources {}))
  ([resources options]
  (-> (core/get-cache-name :static)
      (then (fn [cache-name]
              (core/reset-resources cache-name resources options))))))
