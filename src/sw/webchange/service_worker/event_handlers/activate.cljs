(ns webchange.service-worker.event-handlers.activate
  (:require
    [webchange.service-worker.cache.core :as cache]
    [webchange.service-worker.broadcast.core :as broadcast]
    [webchange.service-worker.config :as config]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.wrappers :refer [catch promise-resolve then]]))

(defn- activate
  []
  (logger/debug "Activate...")
  (broadcast/send-sync-status :activating)
  (cache/remove-old-caches config/cache-names-prefix config/release-number))

(defn- activated
  []
  (logger/log "Activation done.")
  (broadcast/send-sync-status :activated)
  (.claim (.-clients js/self)))

(defn- activation-failed
  [error]
  (logger/error "Activation failed." (.-message error))
  (broadcast/send-sync-status :broken))

(defn handle
  [event]
  (.waitUntil event (-> (activate)
                        (then activated)
                        (catch activation-failed))))
