(ns webchange.service-worker.event-handlers.message
  (:require
    [webchange.service-worker.cache-controller.controller :as cache-controller]
    [webchange.service-worker.common.broadcast :as bc]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.virtual-server.core :as vs]
    [webchange.service-worker.wrappers :refer [then online?]]))

(defn- send-current-state
  []
  (-> (cache-controller/get-current-state)
      (.then bc/send-current-state)))

(defn- handle-cache-course
  [data]
  (when (online?)
    (vs/flush))
  (let [course-name (aget data "course")]
    (-> (cache-controller/cache-course course-name)
        (.then send-current-state))))

(defn- handle-cache-scenes
  [data]
  (let [course-name (aget data "course")
        params {:resources-to-add    (-> data (aget "resources") (aget "add"))
                :resources-to-remove (-> data (aget "resources") (aget "remove"))
                :endpoints-to-add    (-> data (aget "endpoints") (aget "add"))
                :endpoints-to-remove (-> data (aget "endpoints") (aget "remove"))}]
    (-> (cache-controller/cache-scenes params course-name)
        (.then send-current-state))))

(defn handle
  [event]
  (let [message (.-data event)
        type (.-type message)
        data (.-data message)]
    (logger/debug "Get message" type data)
    (case type
      "cache-course" (handle-cache-course data)
      "update-cached-scenes" (handle-cache-scenes data)
      "get-current-state" (send-current-state)
      (logger/warn "Unhandled message type:" type))))
