(ns webchange.service-worker.event-handlers.install
  (:require
    [webchange.service-worker.config :as config]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.virtual-server.core :as vs]
    [webchange.service-worker.wrappers :refer [cache-open cache-add-all js-fetch promise-all response-json then]]))

(defn- get-resources-from-api
  [url]
  (-> (js-fetch url)
      (then response-json)))

(defn- cache-resources
  [cache-name resources]
  (logger/debug-folded (str "Resources to cache into " cache-name) resources)
  (-> (cache-open cache-name)
      (then (fn [cache]
              (logger/debug "Caching" cache-name)
              (cache-add-all cache resources)))))

(defn- load-app-resources
  []
  (-> (get-resources-from-api "/api/resources/app")
      (then (fn [resources]
              (cache-resources (:static config/cache-names) (aget resources "data"))))))

(defn- load-level-resources
  [level]
  (-> (get-resources-from-api (str "/api/resources/level/" level))
      (then (fn [resources]
              (promise-all [(cache-resources (:game config/cache-names) (aget resources "resources"))
                            (vs/install (aget resources "scenes-data"))])))))

(defn- install
  [level]
  (promise-all [(load-app-resources)
                (load-level-resources level)]))

(defn handle
  [event]
  (logger/debug "Install...")
  (let [current-level 1]
    (.waitUntil event (-> (install current-level)
                          (.then #(do (logger/log "Installation done.")
                                      (.skipWaiting js/self)))
                          (.catch #(logger/warn "Installation failed." (.-message %)))))))
