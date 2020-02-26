(ns webchange.service-worker.common.cache
  (:require
    [webchange.service-worker.config :as config]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.virtual-server.core :as vs]
    [webchange.service-worker.wrappers :refer [cache-open cache-add-all cache-keys cache-delete-resource
                                               promise-all promise-resolve response-json then]]))

(defn- cache-resources
  [cache-name resources]
  (logger/debug-folded (str "Resources to cache into " cache-name) resources)
  (-> (cache-open cache-name)
      (then (fn [cache]
              (logger/debug "Caching" cache-name "resources count: " (count resources))
              (loop [left (vec resources)
                     p (promise-resolve nil)]
                (let [limit (min 100 (count left))
                      current (subvec left 0 limit)
                      next (subvec left limit)
                      current-p (then p #(cache-add-all cache current))]
                  (logger/debug "Caching inner count: " (count current))
                  (if (> (count next) 0)
                    (recur next current-p)
                    current-p)))))))

(defn- remove-caches
  [cache-name resources]
  (logger/debug-folded (str "Resources to remove from " cache-name) resources)
  (-> (cache-open cache-name)
      (then (fn [cache]
              (promise-all (map (fn [url]
                                  (cache-delete-resource cache url))
                                resources))))))

(defn- get-cache-resources
  [cache-name]
  (logger/debug-folded (str "Get cache resources from " cache-name))
  (-> (cache-open cache-name)
      (then (fn [cache]
              (cache-keys cache)))))

(defn- get-cached-activity-resources
  []
  (get-cache-resources (:game config/cache-names)))

(defn cache-app-resources
  [resources]
  (cache-resources (:static config/cache-names) resources))

(defn cache-game-resources
  [resources]
  (cache-resources (:game config/cache-names) resources))

(defn remove-game-resources
  [resources]
  (remove-caches (:game config/cache-names) resources))

(defn get-cached-activity-urls
  []
  (-> (get-cached-activity-resources)
      (then (fn [resources]
              (loop [index 0
                     result []
                     count (aget resources "length")]
                (if-not (= index count)
                  (recur (inc index)
                         (conj result (-> resources
                                          (aget index)
                                          (aget "url")
                                          (js/URL.)
                                          (aget "pathname")))
                         count)
                  result))))))

(defn cache-endpoints
  [endpoints]
  (vs/add-endpoints endpoints))
