(ns webchange.service-worker.virtual-server.cache
  (:require
    [webchange.service-worker.config :refer [get-cache-name]]
    [webchange.service-worker.strategies :refer [network-or-cache]]
    [webchange.service-worker.wrappers :refer [cache-open cache-add-all then]]))

(defn cache-all
  [urls course-name]
  (-> (cache-open (get-cache-name :api course-name))
      (then (fn [cache] (cache-add-all cache urls)))))

(defn handle-request
  [request course-name]
  (network-or-cache {:request    request
                     :cache-name (get-cache-name :api course-name)}))
