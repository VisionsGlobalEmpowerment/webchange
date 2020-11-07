(ns webchange.service-worker.virtual-server.cache
  (:require
    [webchange.service-worker.config :refer [get-cache-name]]
    [webchange.service-worker.db.general :as general]
    [webchange.service-worker.strategies :refer [network-or-cache]]
    [webchange.service-worker.wrappers :refer [cache-open cache-add-all cache-keys then request-pathname]]))

(defn cache-all
  [urls]
  (-> (general/get-current-course)
      (then (fn [current-course]
              (cache-open (get-cache-name :api current-course))))
      (then (fn [cache] (cache-add-all cache urls)))))

(defn get-cached
  []
  (-> (general/get-current-course)
      (then (fn [current-course]
              (cache-open (get-cache-name :api current-course))))
      (then (fn [cache]
              (cache-keys cache)))
      (then (fn [keys]
              (map request-pathname keys)))))

(defn handle-request
  [request]
  (-> (general/get-current-course)
      (then (fn [current-course]
              (network-or-cache {:request    request
                                 :cache-name (get-cache-name :api current-course)})))))
