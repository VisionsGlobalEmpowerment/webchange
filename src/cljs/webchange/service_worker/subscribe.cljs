(ns webchange.service-worker.subscribe
  (:require [re-frame.core :as re-frame]
            [webchange.service-worker.events :as events]))

(defn updated-cached-resources-list
  [data]
  (re-frame/dispatch [::events/set-synced-game-resources data]))

(defn subscribe-to-notifications
  []
  (let [channel (js/BroadcastChannel. "sw-messages")]
    (.addEventListener channel "message" (fn [event]
                                           (let [event-data (aget event "data")
                                                 type (aget event-data "type")
                                                 data (-> event-data (aget "data") (js->clj))]
                                             (case type
                                               "get-cached-resources" (updated-cached-resources-list data)
                                               (-> (str "Unhandled service worker message type" type) js/Error. throw)))))))
