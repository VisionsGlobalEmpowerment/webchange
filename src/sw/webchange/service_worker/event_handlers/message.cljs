(ns webchange.service-worker.event-handlers.message
  (:require
    [webchange.service-worker.controllers.worker-state :as worker-state]
    [webchange.service-worker.broadcast.core :as broadcast]
    [webchange.service-worker.controllers.game-resources :as game-resources]
    [webchange.service-worker.db.general :as general]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.wrappers :refer [then online?]]))

(defn- send-current-state
  []
  (-> (worker-state/get-current-state)
      (then broadcast/send-current-state)))

(defn- handle-set-current-course
  [{:keys [course]}]
  (-> (general/set-current-course course)
      (then send-current-state)
      (then (fn []
              (broadcast/send-sync-status :activated)))))

(defn- handle-cache-lessons
  [data]
  (game-resources/update-cached-lessons data))

(defn handle
  [event]
  (let [message (.-data event)
        type (.-type message)
        data (-> (.-data message)
                 (js->clj :keywordize-keys true))]
    (logger/debug "Get message" type data)
    (case type
      "set-current-course" (handle-set-current-course data)
      "cache-lessons" (handle-cache-lessons data)
      "get-current-state" (send-current-state)
      (logger/warn "Unhandled message type:" type))))
