(ns webchange.service-worker.virtual-server.cache
  (:require
    [webchange.service-worker.config :refer [cache-names]]
    [webchange.service-worker.strategies :refer [network-or-cache]]
    [webchange.service-worker.wrappers :refer [cache-open cache-add-all then]]))

(def cache-name (:api cache-names))

(defn cache-all
  [urls]
  (-> (cache-open cache-name)
      (then (fn [cache] (cache-add-all cache urls)))))

(defn handle-request
  [request]
  (network-or-cache {:request    request
                     :cache-name cache-name}))
