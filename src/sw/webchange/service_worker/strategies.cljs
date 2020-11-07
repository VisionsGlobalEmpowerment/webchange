(ns webchange.service-worker.strategies
  (:require
    [clojure.string :refer [starts-with?]]
    [webchange.service-worker.wrappers :refer [cache-match cache-open cache-put
                                               js-fetch promise-resolve then catch
                                               response-clone response-ok? request-url]]))
(defn cache-only
  "Always answering from cache on fetch events."
  [{:keys [request cache-name]}]
  (-> (cache-open cache-name)
      (then #(cache-match % request))))

(defn network-or-cache
  "Tries to retrieve the most up to date content from the network
  but if the request is failed, it will serve cached content instead."
  [{:keys [request cache-name]}]
  (-> (cache-open cache-name)
      (then (fn [cache]
              (-> (js-fetch request)
                  (then (fn [response]
                          (promise-resolve response)))
                  (catch #(cache-match cache request)))))))
