(ns webchange.service-worker.event-handlers.fetch
  (:require
    [clojure.string :refer [starts-with?]]
    [webchange.service-worker.config :refer [get-cache-name release-number]]
    [webchange.service-worker.db.general :as general]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.strategies :as strategy]
    [webchange.service-worker.virtual-server.core :as vs]
    [webchange.service-worker.wrappers :refer [js-fetch online? promise-all promise-reject request-pathname then catch]]
    [webchange.service-worker.controllers.web-app-resources :as web-app-resources]))

(def pages-paths ["/login"
                  "/student-login"
                  "/courses"
                  "/dashboard"
                  "/student-dashboard"])

(defn- belong-paths?
  [request paths]
  (let [pathname (request-pathname request)]
    (some #(starts-with? pathname %) paths)))

(defn- serve-page-skeleton
  []
  (strategy/network-or-cache {:request    web-app-resources/skeleton-page
                              :cache-name (get-cache-name :static)}))

(defn- serve-cache-asset
  [request cache-name]
  (strategy/network-or-cache {:request    request
                              :cache-name cache-name}))

(defn- serve-rest-content
  [request]
  (if (online?)
    (js-fetch request)
    (-> (general/get-current-course)
        (then (fn [current-course]
                (promise-all (map #(serve-cache-asset request %)
                                  [(get-cache-name :static current-course)
                                   (get-cache-name :game current-course)]))))
        (then (fn [responses]
                (let [response (some identity responses)]
                  (if response
                    response
                    (promise-reject (str "Response for " (request-pathname request) " is not cached"))))))
        (catch (fn [error]
                 (promise-reject (str "[Fetch] Serve rest content failed: " error)))))))

(defn- get-response
  [request]
  (cond
    (vs/api-request? request) (vs/handle-request request)
    (belong-paths? request pages-paths) (serve-page-skeleton)
    :else (serve-rest-content request)))

(defn handle
  [event]
  (let [request (.-request event)
        respond (.bind (.-respondWith event) event)]
    (respond (get-response request))))
