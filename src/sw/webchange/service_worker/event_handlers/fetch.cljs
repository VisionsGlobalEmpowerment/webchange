(ns webchange.service-worker.event-handlers.fetch
  (:require
    [clojure.string :refer [starts-with?]]
    [webchange.service-worker.config :refer [cache-names]]
    [webchange.service-worker.utils :refer [log warn]]
    [webchange.wrappers.cache :as cache]
    [webchange.wrappers.fetch :as fetch]
    [webchange.wrappers.request :as request]))

(def pages-paths ["/dashboard"
                  "/student-dashboard"])

(def static-assets-paths ["/css/"
                          "/js/compiled/"])

(defn belong-paths?
  [request paths]
  (let [pathname (request/pathname request)]
    (some #(starts-with? pathname %) paths)))

(defn serve-page-skeleton
  []
  (cache/open
    :cache-name (:static cache-names)
    :then (fn [cache]
            (cache/match
              :cache cache
              :request "./page-skeleton"))))

(defn serve-static-asset
  [request]
  (cache/open
    :cache-name (:static cache-names)
    :then (fn [cache]
            (cache/match
              :cache cache
              :request request
              :then (fn [response]
                      (if response
                        response
                        (do (warn (str "Not matched static: " (request/pathname request)))
                            (fetch/fetch :request request))))))))

(defn fetch-event-handler
  [event]
  (let [request (.-request event)]
    (cond
      (belong-paths? request pages-paths) (.respondWith event (serve-page-skeleton))
      (belong-paths? request static-assets-paths) (.respondWith event (serve-static-asset request))
      :else nil)))
