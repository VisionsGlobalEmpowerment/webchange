(ns webchange.service-worker.event-handlers.activate
  (:require
    [webchange.service-worker.broadcast.core :as broadcast]
    [webchange.service-worker.controllers.course-resources :as course-resources]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.wrappers :refer [catch promise-resolve then]]))

(defn- activate
  []
  (logger/debug "Activate...")
  (broadcast/send-sync-status :activating)
  (course-resources/remove-outdated-data)
  (promise-resolve))

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
