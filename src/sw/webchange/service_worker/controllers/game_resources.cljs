(ns webchange.service-worker.controllers.game-resources
  (:require
    [clojure.set :refer [difference union]]
    [webchange.service-worker.broadcast.core :as broadcast]
    [webchange.service-worker.cache.game :as game-cache]
    [webchange.service-worker.db.state :as db-state]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.requests.api :as api]
    [webchange.service-worker.virtual-server.core :as vs]
    [webchange.service-worker.wrappers :refer [promise-all then]]))

(defn update-cached-lessons
  [update-lessons-data]
  (logger/debug "Update cached lessons data" update-lessons-data)
  (broadcast/send-sync-status :syncing)
  (-> (db-state/get-cached-lessons)
      (then (fn [stored-lessons]
              (let [new-lessons-list (-> (set stored-lessons)
                                         (union (set (:add update-lessons-data)))
                                         (difference (set (:remove update-lessons-data)))
                                         (vec))]
                (logger/debug "Lessons to cache:" new-lessons-list)
                (-> (api/get-game-resources new-lessons-list)
                    (then (fn [{:keys [resources endpoints]}]
                            (logger/debug-folded "Lessons resources to cache" resources)
                            (logger/debug-folded "Lessons endpoints to cache" endpoints)
                            (promise-all [(game-cache/cache-resources resources)
                                          (vs/add-endpoints endpoints)])))
                    (then (fn []
                            (promise-all [(db-state/set-cached-lessons new-lessons-list)
                                          (db-state/set-last-update)])))
                    (then (fn [[new-lessons-list last-update]]
                            (broadcast/send-current-state {:cached-lessons new-lessons-list
                                                           :last-update    last-update})
                            (broadcast/send-sync-status :synced)))))))))
