(ns webchange.service-worker.subscribe
  (:require [re-frame.core :as re-frame]
            [webchange.service-worker.events :as events]))

(defn- set-cached-resources-list
  [data]
  (re-frame/dispatch [::events/set-synced-game-resources data]))

(defn- set-last-update
  [data]
  (let [date (get data "date")
        version (get data "version")]
    (re-frame/dispatch [::events/set-last-update date version])))

(defn subscribe-to-notifications
  []
  (let [channel (js/BroadcastChannel. "sw-messages")]
    (.addEventListener channel "message" (fn [event]
                                           (let [event-data (aget event "data")
                                                 type (aget event-data "type")
                                                 data (-> event-data (aget "data") (js->clj))]
                                             (case type
                                               "get-cached-resources" (set-cached-resources-list data)
                                               "last-update" (set-last-update data)
                                               (-> (str "Unhandled service worker message type" type) js/Error. throw)))))))
