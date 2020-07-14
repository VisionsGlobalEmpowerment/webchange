(ns webchange.service-worker.event-handlers.install
  (:require
    [webchange.service-worker.broadcast.core :as broadcast]
    [webchange.service-worker.controllers.game-resources :as game]
    [webchange.service-worker.controllers.web-app-resources :as web-app]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.wrappers :refer [catch promise-resolve then]]))

(defn- install
  []
  (logger/debug "Install...")
  (broadcast/send-sync-status :installing)
  (-> (web-app/cache-app)
      (then game/restore-cached-lessons)))

(defn- installed
  []
  (logger/log "Installation done.")
  (broadcast/send-sync-status :installed)
  (.skipWaiting js/self))

(defn- installation-failed
  [error]
  (logger/error "Installation failed." (.-message error))
  (broadcast/send-sync-status :broken))

(defn handle
  [event]
  (.waitUntil event (-> (install)
                        (then installed)
                        (catch installation-failed))))
