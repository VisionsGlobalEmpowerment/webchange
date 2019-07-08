(ns webchange.service-worker.event-handlers.fetch
  (:require
    [clojure.string :refer [starts-with?]]
    [webchange.service-worker.config :refer [cache-names]]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.strategies :as strategy]
    [webchange.service-worker.virtual-server.core :as vs]
    [webchange.service-worker.wrappers :refer [js-fetch promise-all request-pathname then]]))

(def pages-paths ["/courses"
                  "/dashboard"
                  "/student-dashboard"])

(defn- belong-paths?
  [request paths]
  (let [pathname (request-pathname request)]
    (some #(starts-with? pathname %) paths)))

(defn- serve-page-skeleton
  []
  (strategy/cache-only {:request    "./page-skeleton"
                        :cache-name (:static cache-names)}))

(defn- serve-cache-asset
  [request cache-name]
  (strategy/cache-only {:request    request
                        :cache-name cache-name}))

(defn- serve-rest-content
  [request]
  (let [cache-names [(:static cache-names)
                     (:game cache-names)]
        match-promises (map #(serve-cache-asset request %) cache-names)]
    (-> match-promises
        (promise-all)
        (then (fn [responses]
                 (let [response (some identity responses)]
                   (if response
                     response
                     (do (logger/debug (str "Not matched: " (request-pathname request)))
                         (js-fetch request)))))))))

(defn handle
  [event]
  (let [request (.-request event)
        respond (.bind (.-respondWith event) event)]
    (cond
      (vs/api-request? request) (respond (vs/handle-request request))
      (belong-paths? request pages-paths) (respond (serve-page-skeleton))
      :else (respond (serve-rest-content request)))))
