(ns webchange.service-worker.event-handlers.message
  (:require
    [webchange.service-worker.broadcast.core :as broadcast]
    [webchange.service-worker.controllers.course-resources :as course-resources]
    [webchange.service-worker.controllers.game-resources :as game-resources]
    [webchange.service-worker.controllers.worker-state :as worker-state]
    [webchange.service-worker.db.general :as general]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.virtual-server.handlers.current-progress :as current-progress]
    [webchange.service-worker.wrappers :refer [catch online? promise-resolve then]]))

(defn- send-current-state
  []
  (logger/debug "Send current state...")
  (-> (current-progress/flush-current-progress)
      (then worker-state/get-current-state)
      (then broadcast/send-current-state)
      (then #(broadcast/send-sync-status :synced))
      (catch (fn [error]
               (logger/error "Send current state failed:" error)))))

(defn- set-current-course
  [course]
  (logger/debug "[Message] Set current course:" course)
  (-> (general/set-current-course course)
      (then (fn [] (course-resources/fetch-current-course-data)))
      (then (fn [] (course-resources/remove-outdated-data)))
      (then send-current-state)))


(defn- handle-set-current-course
  [{:keys [course]}]
  (-> (general/get-current-course)
      (then (fn [current-course]
              (logger/debug (str "[Message] Change course: " current-course "->" course))
              (if-not (= course current-course)
                (set-current-course course)
                (do (logger/debug "[Message] Course not changed")
                    (send-current-state)))))
      (catch (fn [error]
               (logger/warn "[Message] Get current course failed:" error)
               (set-current-course course)))))

(defn- handle-cache-resources
  [data]
  (game-resources/update-cached-resources data))

(defn handle
  [event]
  (let [message (.-data event)
        type (.-type message)
        data (-> (.-data message)
                 (js->clj :keywordize-keys true))]
    (logger/debug (str "[Message] Get message  <" type ">"))
    (logger/debug-folded (str "[Message] <" type "> message data:") data)
    (case type
      "set-current-course" (handle-set-current-course data)
      "cache-resources" (handle-cache-resources data)
      "get-current-state" (send-current-state)
      (logger/warn "Unhandled message type:" type))))
