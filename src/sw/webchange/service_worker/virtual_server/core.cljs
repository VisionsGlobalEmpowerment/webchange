(ns webchange.service-worker.virtual-server.core
  (:require
    [clojure.data :refer [diff]]
    [clojure.string :refer [starts-with?]]
    [webchange.service-worker.config :refer [api-path]]
    [webchange.service-worker.virtual-server.cache :as cache]
    [webchange.service-worker.virtual-server.handlers.core :as handlers]
    [webchange.service-worker.wrappers :refer [promise-all request-pathname]]))

(defn add-endpoints
  [endpoints course-name]
  (let [has-not? (fn [vector value] (->> vector (some #(= value %)) not))
        routes-to-prefetch (filter handlers/has-handler? endpoints)
        routes-to-cache (filter #(has-not? routes-to-prefetch %) endpoints)]
    (promise-all [(cache/cache-all routes-to-cache course-name)
                  (handlers/prefetch routes-to-prefetch)])))

(defn api-request?
  [request]
  (let [pathname (request-pathname request)]
    (starts-with? pathname api-path)))

(defn handle-request
  [request course-name]
  (if (handlers/has-handler? request)
    (handlers/handle-request request course-name)
    (cache/handle-request request course-name)))

(defn flush
  []
  (handlers/flush))
