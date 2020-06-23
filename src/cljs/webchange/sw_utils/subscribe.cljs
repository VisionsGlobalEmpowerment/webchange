(ns webchange.sw-utils.subscribe
  (:require [re-frame.core :as re-frame]
            [webchange.config :as config]
            [webchange.sw-utils.state.status :as status]
            [webchange.sw-utils.state.resources :as resources]))

(defn- set-current-state
  [data]
  (if-let [version (:version data)]
    (re-frame/dispatch [::status/set-version version]))

  (if-let [last-update (:last-update data)]
    (re-frame/dispatch [::status/set-last-update last-update]))

  (if-let [resources (:game-resources data)]
    (re-frame/dispatch [::resources/set-synced-game-resources resources]))

  (if-let [endpoints (:game-endpoints data)]
    (re-frame/dispatch [::resources/set-synced-game-endpoints endpoints]))

  (re-frame/dispatch [::status/set-sync-status :synced]))

(defn- handle-error
  [error]
  (when config/debug?
    (re-frame/dispatch [::status/handle-error error])))

(defn subscribe-to-notifications
  []
  (let [channel (js/BroadcastChannel. "sw-messages")]
    (.addEventListener channel "message" (fn [event]
                                           (let [event-data (aget event "data")
                                                 type (aget event-data "type")
                                                 data (-> event-data (aget "data") (js->clj :keywordize-keys true))]
                                             (case type
                                               "current-state" (set-current-state data)
                                               "error" (handle-error data)
                                               (-> (str "Unhandled service worker message type " type) js/Error. throw)))))))
