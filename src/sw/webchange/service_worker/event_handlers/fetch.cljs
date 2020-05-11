(ns webchange.service-worker.event-handlers.fetch
  (:require
    [clojure.string :refer [starts-with?]]
    [webchange.service-worker.common.db :refer [get-current-course]]
    [webchange.service-worker.config :refer [get-cache-name]]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.strategies :as strategy]
    [webchange.service-worker.virtual-server.core :as vs]
    [webchange.service-worker.wrappers :refer [js-fetch promise-all request-pathname then catch]]))

(def pages-paths ["/student-login"
                  "/courses"
                  "/dashboard"
                  "/student-dashboard"])

(defn- belong-paths?
  [request paths]
  (let [pathname (request-pathname request)]
    (some #(starts-with? pathname %) paths)))

(defn- serve-page-skeleton
  [course-name]
  (strategy/cache-only {:request    "./page-skeleton"
                        :cache-name (get-cache-name :static course-name)}))

(defn- serve-cache-asset
  [request cache-name]
  (strategy/cache-only {:request    request
                        :cache-name cache-name}))

(defn- serve-rest-content
  [request course-name]
  (let [cache-names [(get-cache-name :static course-name)
                     (get-cache-name :game course-name)]
        match-promises (map #(serve-cache-asset request %) cache-names)]
    (-> match-promises
        (promise-all)
        (then (fn [responses]
                 (let [response (some identity responses)]
                   (if response
                     response
                     (do (logger/debug (str "Not matched: " (request-pathname request)))
                         (js-fetch request))))))
        (catch #(js-fetch request)))))

(defn- get-response
  [request]
  (-> (get-current-course)
      (then (fn [course-name]
              (cond
                (vs/api-request? request) (vs/handle-request request course-name)
                (belong-paths? request pages-paths) (serve-page-skeleton course-name)
                :else (serve-rest-content request course-name))))
      (catch #(js-fetch request))))

(defn handle
  [event]
  (let [request (.-request event)
        respond (.bind (.-respondWith event) event)]
    (respond (get-response request))))
