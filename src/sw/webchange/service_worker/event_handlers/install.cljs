(ns webchange.service-worker.event-handlers.install
  (:require
    [webchange.service-worker.config :as config]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.virtual-server.core :as vs]
    [webchange.service-worker.wrappers.cache :as cache]
    [webchange.service-worker.wrappers.fetch :as fetch]
    [webchange.service-worker.wrappers.promise :as promise]))

(defn- get-resources-from-api
  [& {:keys [url then]}]
  (fetch/fetch
    :request url
    :then (fn [response]
            (-> (.json response)
                (.then then)))))

(defn- cache-resources
  [cache-name resources]
  (logger/debug-folded (str "Resources to cache into " cache-name) resources)
  (cache/open
    :cache-name cache-name
    :then (fn [cache]
            (logger/debug "Caching" cache-name)
            (cache/add-all
              :cache cache
              :requests resources))))

(defn- load-app-resources
  []
  (get-resources-from-api
    :url "/api/resources/app"
    :then (fn [resources]
            (cache-resources (:static config/cache-names) (aget resources "data")))))

(defn- load-level-resources
  [level]
  (get-resources-from-api
    :url (str "/api/resources/level/" level)
    :then (fn [resources]
            (promise/all [(cache-resources (:game config/cache-names) (aget resources "resources"))
                          (vs/install (aget resources "scenes-data"))]))))

(defn- install
  [level]
  (promise/all [(load-app-resources)
                (load-level-resources level)]))

(defn handle
  [event]
  (logger/debug "Install...")
  (let [current-level 1]
    (.waitUntil event (-> (install current-level)
                          (.then #(do (logger/log "Installation done.")
                                      (.skipWaiting js/self)))
                          (.catch #(logger/warn "Installation failed." (.-message %)))))))
