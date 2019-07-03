(ns webchange.service-worker.event-handlers.install
  (:require
    [webchange.service-worker.config :as config]
    [webchange.service-worker.utils :refer [log log-folded warn]]
    [webchange.wrappers.cache :as cache]
    [webchange.wrappers.fetch :as fetch]))

(defn get-resources-from-api
  [& {:keys [url then]}]
  (fetch/fetch
    :request url
    :then (fn [response]
            (-> (.json response)
                (.then then)))))

(defn cache-resources
  [cache-name resources]
  (log-folded (str "Resources to cache into " cache-name) resources)
  (cache/open
    :cache-name cache-name
    :then (fn [cache]
            (log "Caching" cache-name)
            (cache/add-all
              :cache cache
              :requests resources))))

(defn group-promises
  [& promises]
  (->> promises
       (clj->js)
       (js/Promise.all)))

(defn load-app-resources
  []
  (get-resources-from-api
    :url "/api/resources/app"
    :then (fn [resources]
            (cache-resources (:static config/cache-names) (aget resources "data")))))

(defn load-level-resources
  [level]
  (get-resources-from-api
    :url (str "/api/resources/level/" level)
    :then (fn [resources]
            (group-promises
              (cache-resources (:game config/cache-names) (aget resources "resources"))
              (cache-resources (:api config/cache-names) (aget resources "scenes-data"))))))

(defn- install
  [level]
  (group-promises
    (load-app-resources)
    (load-level-resources level)))

(defn install-event-handler
  [event]
  (log "Install...")
  (.waitUntil event (-> (install 1)
                        (.then #(log "Installation done."))
                        (.catch #(warn "Installation failed." (.-message %))))))