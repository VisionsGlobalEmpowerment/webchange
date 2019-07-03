(ns webchange.service-worker.event-handlers.fetch
  (:require
    [clojure.string :refer [starts-with?]]
    [webchange.service-worker.config :refer [cache-names]]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.utils :refer [group-promises]]
    [webchange.wrappers.cache :as cache]
    [webchange.wrappers.fetch :as fetch]
    [webchange.wrappers.request :as request]))

(def api-paths ["/api/"])

(def pages-paths ["/dashboard"
                  "/student-dashboard"])

(defn belong-paths?
  [request paths]
  (let [pathname (request/pathname request)]
    (some #(starts-with? pathname %) paths)))

(defn serve-api-request
  [request]
  (cache/open
    :cache-name (:api cache-names)
    :then (fn [cache]
            (fetch/fetch
              :request request
              :then (fn [response]
                      (cache/put
                        :cache cache
                        :request request
                        :response response
                        :then #(logger/debug "API response cached."))
                      response)
              :catch (fn [error]
                       (logger/debug "API request FAILED:" error)
                       (logger/debug "Serving response from cache..")
                       (cache/match
                         :cache cache
                         :request request))))))

(defn serve-page-skeleton
  []
  (cache/open
    :cache-name (:static cache-names)
    :then (fn [cache]
            (cache/match
              :cache cache
              :request "./page-skeleton"))))

(defn serve-cache-asset
  [request cache-name]
  (cache/open
    :cache-name cache-name
    :then (fn [cache]
            (cache/match
              :cache cache
              :request request))))

(defn serve-rest-content
  [request]
  (let [cache-names [(:static cache-names)
                     (:game cache-names)]
        match-promises (map #(serve-cache-asset request %) cache-names)]
    (-> match-promises
        (clj->js)
        (js/Promise.all)
        (.then (fn [responses]
                 (let [response (some identity responses)]
                   (if response
                     response
                     (do (logger/debug (str "Not matched: " (request/pathname request)))
                         (fetch/fetch :request request)))))))))

(defn method-filter
  [method event handler]
  (let [request (.-request event)
        current-method (.-method request)
        respond (.bind (.-respondWith event) event)]
    (when (= method current-method) (handler request respond))))

(defn fetch-event-handler
  [event]
  (let [GET #(method-filter "GET" event %)]
    (GET (fn [request respond]
           (cond
             (belong-paths? request api-paths) (respond (serve-api-request request))
             (belong-paths? request pages-paths) (respond (serve-page-skeleton))
             :else (respond (serve-rest-content request)))))))
