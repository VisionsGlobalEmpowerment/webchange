(ns webchange.service-worker.event-handlers.activate
  (:require
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.wrappers :refer [catch promise-resolve then]]))

(defn- activate
  []
  (promise-resolve))

(defn handle
  [event]
  (logger/debug "Activate...")
  (.waitUntil event (-> (activate)
                        (then #(logger/log "Activation done."))
                        (catch #(logger/warn "Activation failed." (.-message %))))))
