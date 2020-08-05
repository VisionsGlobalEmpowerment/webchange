(ns webchange.service-worker.virtual-server.core
  (:require
    [clojure.data :refer [diff]]
    [clojure.string :refer [starts-with?]]
    [webchange.service-worker.config :refer [api-path]]
    [webchange.service-worker.virtual-server.cache :as cache]
    [webchange.service-worker.virtual-server.handlers.core :as handlers]
    [webchange.service-worker.virtual-server.logger :as logger]
    [webchange.service-worker.wrappers :refer [catch js-fetch promise-all promise-reject request-pathname then online?]]))

(defn add-endpoints
  [endpoints]
  (logger/debug-folded "Add endpoints" endpoints)
  (let [has-not? (fn [vector value] (->> vector (some #(= value %)) not))
        routes-to-prefetch (filter handlers/has-handler? endpoints)
        routes-to-cache (filter #(has-not? routes-to-prefetch %) endpoints)]
    (-> (promise-all [(cache/cache-all routes-to-cache)
                      (handlers/prefetch routes-to-prefetch)])
        (catch (fn [error]
                 (logger/warn "Can not add endpoints" error)
                 (promise-reject error))))))

(defn api-request?
  [request]
  (let [pathname (request-pathname request)]
    (starts-with? pathname api-path)))

(defn handle-request
  [request]
  (if (handlers/has-handler? request)
    (handlers/handle-request request)
    (if (online?)
      (js-fetch request)
      (cache/handle-request request))))

(defn flush-state
  []
  (handlers/flush-state))
