(ns webchange.sw-utils.subscribe
  (:require [re-frame.core :as re-frame]
            [webchange.sw-utils.state.status :as status]
            [webchange.sw-utils.state.resources :as resources]))

(defn- set-cached-resources-list
  [data]
  (re-frame/dispatch [::resources/set-synced-game-resources data]))

(defn- set-last-update
  [data]
  (let [date (get data "date")
        version (get data "version")]
    (re-frame/dispatch [::status/set-last-update date version])
    (re-frame/dispatch [::status/set-sync-status :synced])))

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
