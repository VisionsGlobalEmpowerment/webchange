(ns webchange.service-worker.event-handlers.message
  (:require
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.config :as config]
    [webchange.service-worker.common.cache :refer [cache-game-resources
                                                   cache-endpoints
                                                   get-cached-activity-urls
                                                   remove-game-resources]]
    [webchange.service-worker.common.db :as db]
    [webchange.service-worker.common.fetch :refer [fetch-game-start-resources]]
    [webchange.service-worker.wrappers :refer [then promise-all js-fetch response-json]]))

(def broadcast-channel "sw-messages")
(def message-types {:last-update      "last-update"
                    :cached-resources "get-cached-resources"})

(defn- broadcast-message
  [message]
  (let [channel (js/BroadcastChannel. broadcast-channel)]
    (.postMessage channel (clj->js message))))

(defn- update-cached-scenes
  [data]
  (let [resources-to-add (-> data (aget "resources") (aget "add"))
        resources-to-remove (-> data (aget "resources") (aget "remove"))
        scenes (-> data (aget "scenes") (aget "add"))]
    (logger/debug "Update cached resources")
    (logger/debug "Add:" resources-to-add)
    (logger/debug "Remove:" resources-to-remove)
    (cache-game-resources resources-to-add)
    (remove-game-resources resources-to-remove)
    (logger/debug "Update cached scenes" scenes)
    (cache-endpoints scenes)))

(defn- send-get-cached-resources
  []
  (logger/debug "Get cached resources")
  (-> (get-cached-activity-urls)
      (then (fn [urls]
              (broadcast-message {:type (:cached-resources message-types)
                                  :data urls})))))

(defn- load-game-start-resources
  [course-name]
  (-> (fetch-game-start-resources course-name)
      (then (fn [resources]
              (promise-all [(-> resources (aget "resources") (cache-game-resources))
                            (-> resources (aget "endpoints") (cache-endpoints))])))))

(defn- cache-start-activities
  [data]
  (let [course (aget data "course")]
    (logger/debug "Cache start activities" course)
    (load-game-start-resources course)))

(defn send-last-update
  []
  (db/get-value "last-update" (fn [date]
                                (broadcast-message {:type (:last-update message-types)
                                                    :data {:date    date
                                                           :version config/release-number}}))))

(defn handle
  [event]
  (let [message (.-data event)
        type (.-type message)
        data (.-data message)]
    (logger/debug "Get message" type data)
    (case type
      "update-cached-scenes" (update-cached-scenes data)
      "get-cached-resources" (send-get-cached-resources)
      "get-last-update" (send-last-update)
      "cache-start-activities" (cache-start-activities data)
      (logger/warn "Unhandled message type:" type))))
