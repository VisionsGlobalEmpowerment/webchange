(ns webchange.service-worker.event-handlers.fetch
  (:require
    [clojure.string :refer [starts-with?]]
    [webchange.service-worker.config :refer [cache-names]]
    [webchange.service-worker.utils :refer [debug log warn]]
    [webchange.wrappers.cache :as cache]
    [webchange.wrappers.fetch :as fetch]
    [webchange.wrappers.request :as request]))

(def api-paths ["/api/"])

(def pages-paths ["/dashboard"
                  "/student-dashboard"])

(def static-assets-paths ["/css/"
                          "/js/compiled/"])

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
                        :then (fn []
                                (debug "API response cached.")
                                ))
                      response)
              :catch (fn [error]
                       (debug "API request FAILED:" error)
                       (debug "Serving response from cache..")
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

;(defn serve-static-asset
;  [request]
;  (cache/open
;    :cache-name (:static cache-names)
;    :then (fn [cache]
;            (cache/match
;              :cache cache
;              :request request
;              :then (fn [response]
;                      (if response
;                        response
;                        (do (warn (str "Not matched static: " (request/pathname request)))
;                            (fetch/fetch :request request))))))))

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
                     (do (warn (str "Not matched: " (request/pathname request)))
                         (fetch/fetch :request request)))))))))

(defn fetch-event-handler
  [event]
  (let [request (.-request event)]
    (cond
      (belong-paths? request api-paths) (.respondWith event (serve-api-request request))
      (belong-paths? request pages-paths) (.respondWith event (serve-page-skeleton))
      :else (.respondWith event (serve-rest-content request)))))
