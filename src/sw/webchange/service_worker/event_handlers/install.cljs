(ns webchange.service-worker.event-handlers.install
  (:require
    [webchange.service-worker.config :as config]
    [webchange.service-worker.utils :refer [log log-folded]]
    [webchange.wrappers.cache :as cache]
    [webchange.wrappers.fetch :as fetch]))

(defn get-app-assets
  [& {:keys [then]}]
  (fetch/fetch
    :request "/api/resources/app"
    :then (fn [response]
            (-> (.json response)
                (.then #(then (.-data %)))))))

(defn- install
  []
  (get-app-assets
    :then (fn [app-assets]
            (log-folded "App assets" app-assets)
            (cache/open
              :cache-name (:static config/cache-names)
              :then (fn [cache]
                      (cache/add-all
                        :cache cache
                        :requests app-assets)))))
  )

(defn install-event-handler
  [event]
  (log "Install..")
  (.waitUntil event (install)))