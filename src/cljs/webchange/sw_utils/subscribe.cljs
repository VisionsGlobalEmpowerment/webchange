(ns webchange.sw-utils.subscribe
  (:require [re-frame.core :as re-frame]
            [webchange.config :as config]
            [webchange.routes :as routes]
            [webchange.sw-utils.state.resources :as resources]
            [webchange.sw-utils.state.status :as status]))

(defn- set-current-state
  [data]
  (if-let [version (:version data)]
    (re-frame/dispatch [::status/set-version version]))

  (if-let [last-update (:last-update data)]
    (re-frame/dispatch [::status/set-last-update last-update]))

  (if-let [cached-lessons (:cached-lessons data)]
    (re-frame/dispatch [::resources/set-cached-lessons cached-lessons])))

(defn- set-sync-status
  [value]
  (re-frame/dispatch [::status/set-sync-status (keyword value)]))

(defn- handle-error
  [error]
  (when config/debug?
    (re-frame/dispatch [::status/handle-error error])))

(defn- handle-redirect
  [redirect-target]
  (case redirect-target
    "login" (routes/redirect-to :student-login)))

(defn subscribe-to-notifications
  []
  (let [channel (js/BroadcastChannel. "sw-messages")]
    (.addEventListener channel "message" (fn [event]
                                           (let [event-data (aget event "data")
                                                 type (aget event-data "type")
                                                 data (-> event-data (aget "data") (js->clj :keywordize-keys true))]
                                             (case type
                                               "current-state" (set-current-state data)
                                               "sync-status" (set-sync-status data)
                                               "error" (handle-error data)
                                               "redirect" (handle-redirect data)
                                               (-> (str "Unhandled service worker message type " type) js/Error. throw)))))))
