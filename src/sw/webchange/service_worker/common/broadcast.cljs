(ns webchange.service-worker.common.broadcast)

(defn- broadcast-message
  [key data]
  (let [broadcast-channel "sw-messages"
        type (get {:current-state "current-state"} key)]
    (-> (js/BroadcastChannel. broadcast-channel)
        (.postMessage (clj->js {:type type
                                :data data})))))

(defn send-current-state
  [resources]
  (broadcast-message :current-state resources))
