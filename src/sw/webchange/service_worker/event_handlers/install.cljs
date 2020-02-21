(ns webchange.service-worker.event-handlers.install
  (:require
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.wrappers :refer [catch promise-resolve then]]))

(defn- install
  []
  (promise-resolve))

(defn handle
  [event]
  (logger/debug "Install...")
  (.waitUntil event (-> (install)
                        (then #(do (logger/log "Installation done.")
                                   (.skipWaiting js/self)))
                        (catch #(do (logger/warn "Installation failed." (.-message %))
                                    (throw %))))))
