(ns webchange.service-worker.broadcast.core
  (:require
    [webchange.service-worker.logger :as logger]))

(def broadcast-channel "sw-messages")
(def messages {:current-state "current-state"
               :sync-status   "sync-status"
               :error         "error"
               :redirect      "redirect"})

(defn- broadcast-message
  [key data]
  (let [type (get messages key)]
    (logger/debug-folded (str "[Broadcast] Send message: <" type ">") data)
    (-> (js/BroadcastChannel. broadcast-channel)
        (.postMessage (clj->js {:type type
                                :data data})))))

(defn send-current-state
  [data]
  (broadcast-message :current-state data))

(defn send-sync-status
  [status]
  (broadcast-message :sync-status (name status)))

(defn send-error
  [message error]
  (broadcast-message :error {:message message
                             :error   error}))

(defn redirect-to-login
  []
  (broadcast-message :redirect "login"))
