(ns webchange.service-worker.event-handlers.fetch
  (:require
    [clojure.string :refer [starts-with?]]
    [webchange.service-worker.config :refer [cache-names]]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.virtual-server.core :as vs]
    [webchange.service-worker.wrappers.cache :as cache]
    [webchange.service-worker.wrappers.fetch :as fetch]
    [webchange.service-worker.wrappers.promise :as promise]
    [webchange.service-worker.wrappers.request :as request]))

(def pages-paths ["/courses"
                  "/dashboard"
                  "/student-dashboard"])

(defn- belong-paths?
  [request paths]
  (let [pathname (request/pathname request)]
    (some #(starts-with? pathname %) paths)))

(defn- serve-page-skeleton
  []
  (cache/open
    :cache-name (:static cache-names)
    :then (fn [cache]
            (cache/match
              :cache cache
              :request "./page-skeleton"))))

(defn- serve-cache-asset
  [request cache-name]
  (cache/open
    :cache-name cache-name
    :then (fn [cache]
            (cache/match
              :cache cache
              :request request))))

(defn- serve-rest-content
  [request]
  (let [cache-names [(:static cache-names)
                     (:game cache-names)]
        match-promises (map #(serve-cache-asset request %) cache-names)]
    (-> match-promises
        (promise/all)
        (.then (fn [responses]
                 (let [response (some identity responses)]
                   (if response
                     response
                     (do (logger/debug (str "Not matched: " (request/pathname request)))
                         (fetch/fetch :request request)))))))))

(defn- method-filter
  [method event handler]
  (let [request (.-request event)
        current-method (.-method request)
        respond (.bind (.-respondWith event) event)]
    (when (= method current-method) (handler request respond))))

(defn handle
  [event]
  (let [request (.-request event)
        respond (.bind (.-respondWith event) event)]
    (cond
      (vs/api-request? request) (respond (vs/handle-request request))
      (belong-paths? request pages-paths) (respond (serve-page-skeleton))
      :else (respond (serve-rest-content request)))))
