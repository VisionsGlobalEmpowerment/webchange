(ns webchange.service-worker.event-handlers.install
  (:require
    [webchange.service-worker.config :as config]
    [webchange.service-worker.utils :refer [log]]
    [webchange.wrappers.cache :as cache]))

(def static-assets
  ["/raw/img/ui/dashboard/scene-preview/Casa_Room.jpg"])

(defn- install
  []
  (cache/open
    :cache-name (:static config/cache-names)
    :then (fn [cache]
            (cache/add-all
              :cache cache
              :requests (clj->js static-assets)))))

(defn install-event-handler
  [event]
  (log "Install..")
  (.waitUntil event (install)))