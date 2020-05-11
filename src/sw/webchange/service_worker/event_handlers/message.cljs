(ns webchange.service-worker.event-handlers.message
  (:require
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.common.cache :refer [get-cached-activity-urls]]
    [webchange.service-worker.common.db :as db]
    [webchange.service-worker.common.broadcast :refer [send-cached-resources
                                                       send-last-update]]
    [webchange.service-worker.wrappers :refer [then]]
    [webchange.service-worker.cache-controller.controller :as cache-controller]))

(defn- handle-cache-course
  [data]
  (let [course-name (aget data "course")]
    (cache-controller/cache-course course-name)))

(defn- handle-cache-scenes
  [data]
  (let [course-name (aget data "course")
        params {:resources-to-add    (-> data (aget "resources") (aget "add"))
                :resources-to-remove (-> data (aget "resources") (aget "remove"))
                :endpoints-to-add    (-> data (aget "scenes") (aget "add"))
                :endpoints-to-remove (-> data (aget "scenes") (aget "remove"))}]
    (cache-controller/cache-scenes params course-name)))

(defn- handle-get-cached-resources
  [data]
  (let [course-name (aget data "course")]
    (-> (get-cached-activity-urls course-name)
        (then (fn [urls]
                (send-cached-resources urls))))))

(defn- handle-get-last-update
  []
  (-> (db/get-value "last-update")
      (then #(send-last-update %))))

(defn handle
  [event]
  (let [message (.-data event)
        type (.-type message)
        data (.-data message)]
    (logger/debug "Get message" type data)
    (case type
      "cache-course" (handle-cache-course data)
      "update-cached-scenes" (handle-cache-scenes data)
      "get-cached-resources" (handle-get-cached-resources data)
      "get-last-update" (handle-get-last-update)
      (logger/warn "Unhandled message type:" type))))
