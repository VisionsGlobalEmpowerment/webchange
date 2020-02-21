(ns webchange.service-worker.common.broadcast
  (:require
    [webchange.service-worker.config :as config]))

(defn- broadcast-message
  [key data]
  (let [broadcast-channel "sw-messages"
        type (get {:last-update      "last-update"
                   :cached-resources "get-cached-resources"} key)]
    (-> (js/BroadcastChannel. broadcast-channel)
        (.postMessage (clj->js {:type type
                                :data data})))))

(defn send-last-update
  [date]
  (broadcast-message :last-update {:date    date
                                   :version config/release-number}))

(defn send-cached-resources
  [resources]
  (broadcast-message :cached-resources resources))
