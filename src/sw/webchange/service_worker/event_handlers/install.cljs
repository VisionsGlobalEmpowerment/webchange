(ns webchange.service-worker.event-handlers.install
  (:require
    [webchange.service-worker.broadcast.core :as broadcast]
    [webchange.service-worker.controllers.course-resources :as course-resources]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.wrappers :refer [catch promise-resolve then]]))

(defn- install
  []
  (logger/debug "Install...")
  (broadcast/send-sync-status :installing)
  (-> (course-resources/fetch-current-course-data)
      (catch (fn [error]
               (logger/warn "Install: resources caching skipped." error)
               (promise-resolve))))
  (promise-resolve))

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
