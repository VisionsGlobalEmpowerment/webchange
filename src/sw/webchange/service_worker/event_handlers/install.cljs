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
                (.then #(then (.-data %)))))))

(defn load-and-cache-resources
  [url cache-name]
  (get-resources-from-api
    :url url
    :then (fn [resources]
            (log-folded (str "Resources to cache into " cache-name) resources)
            (cache/open
              :cache-name cache-name
              :then (fn [cache]
                      (log "Caching" cache-name)
                      (cache/add-all
                        :cache cache
                        :requests resources))))))

(defn- install
  []
  (->> [(load-and-cache-resources "/api/resources/app" (:static config/cache-names))
        (load-and-cache-resources "/api/resources/level/1" (:game config/cache-names))]
       (clj->js)
       (js/Promise.all)))

(defn install-event-handler
  [event]
  (log "Install...")
  (.waitUntil event (-> (install)
                        (.then #(log "Installation done."))
                        (.catch #(warn "Installation failed." (.-message %))))))