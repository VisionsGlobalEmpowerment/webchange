(ns webchange.service-worker.event-handlers.message
  (:require
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.common.cache :refer [cache-game-resources
                                                   cache-endpoints
                                                   get-cached-activity-urls]]
    [webchange.service-worker.wrappers :refer [then promise-all js-fetch response-json]]
    [webchange.service-worker.common.fetch :refer [fetch-game-start-resources]]))

;; ToDo: support removing extra caches
(defn- update-cached-scenes
  [data]
  (let [resources (-> data (aget "resources") (aget "add"))
        scenes (-> data (aget "scenes") (aget "add"))]
    (logger/debug "Update cached scenes:" scenes resources)
    (cache-game-resources resources)
    (cache-endpoints scenes)))

(defn broadcast-message
  [message]
  (let [channel (js/BroadcastChannel. "sw-messages")]
    (.postMessage channel (clj->js message))))

(defn get-cached-resources
  []
  (logger/debug "Get cached resources")
  (-> (get-cached-activity-urls)
      (then (fn [urls]
              (broadcast-message {:type "get-cached-resources"
                                  :data urls})))))

(defn- load-game-start-resources
  [course-name]
  (-> (fetch-game-start-resources course-name)
      (then (fn [resources]
              (promise-all [(-> resources (aget "resources") (cache-game-resources))
                            (-> resources (aget "endpoints") (cache-endpoints))])))))

(defn cache-start-activities
  [data]
  (let [course (aget data "course")]
    (logger/debug "Cache start activities" course)
    (load-game-start-resources course)))

(defn handle
  [event]
  (let [message (.-data event)
        type (.-type message)
        data (.-data message)]
    (logger/debug "Get message" type data)
    (case type
      "update-cached-scenes" (update-cached-scenes data)
      "get-cached-resources" (get-cached-resources)
      "cache-start-activities" (cache-start-activities data)
      (logger/warn "Unhandled message type:" type))))
