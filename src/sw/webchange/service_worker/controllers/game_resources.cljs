(ns webchange.service-worker.controllers.game-resources
  (:require
    [clojure.set :refer [difference union]]
    [webchange.service-worker.broadcast.core :as broadcast]
    [webchange.service-worker.cache.game :as game-cache]
    [webchange.service-worker.db.state :as db-state]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.requests.api :as api]
    [webchange.service-worker.virtual-server.core :as vs]
    [webchange.service-worker.wrappers :refer [promise-all promise-resolve then]]))

(defn- cache-lessons
  [lessons-list]
  (logger/debug "Cache lessons" lessons-list)
  (broadcast/send-sync-status :syncing)
  (-> (api/get-game-resources lessons-list)
      (then (fn [{:keys [resources endpoints]}]
              (logger/debug-folded "Lessons resources to cache" resources)
              (logger/debug-folded "Lessons endpoints to cache" endpoints)
              (promise-all [(game-cache/cache-resources resources)
                            (vs/add-endpoints endpoints)])))
      (then (fn []
              (promise-all [(db-state/set-cached-lessons lessons-list)
                            (db-state/set-last-update)])))
      (then (fn [[new-lessons-list last-update]]
              (broadcast/send-current-state {:cached-lessons new-lessons-list
                                             :last-update    last-update})
              (broadcast/send-sync-status :synced)
              (promise-resolve lessons-list)))))

(defn update-cached-lessons
  [update-lessons-data]
  (logger/debug "Update cached lessons data" update-lessons-data)
  (-> (db-state/get-cached-lessons)
      (then (fn [stored-lessons]
              (let [new-lessons-list (-> (set stored-lessons)
                                         (union (set (:add update-lessons-data)))
                                         (difference (set (:remove update-lessons-data)))
                                         (vec))]
                (cache-lessons new-lessons-list))))))

(defn restore-cached-lessons
  []
  (logger/debug "Restore cached lessons data")
  (-> (db-state/get-cached-lessons)
      (then (fn [stored-lessons]
              (cache-lessons stored-lessons)))))
