(ns webchange.service-worker.controllers.game-resources
  (:require
    [clojure.set :refer [difference union]]
    [webchange.service-worker.broadcast.core :as broadcast]
    [webchange.service-worker.cache.game :as game-cache]
    [webchange.service-worker.db.state :as db-state]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.virtual-server.core :as vs]
    [webchange.service-worker.wrappers :refer [promise-all promise-resolve then]]))

(defn- cache-resources
  [resources endpoints]
  (logger/debug-folded "Resources to cache" resources)
  (logger/debug-folded "Endpoints to cache" endpoints)
  (broadcast/send-sync-status :syncing)
  (-> (promise-all [(game-cache/reset-resources resources
                                                {:on-progress #(broadcast/send-current-state {:caching-progress %})})
                    (vs/add-endpoints endpoints)])
      (then (fn []
              (promise-all [
                            ;(db-state/set-cached-resources {:resources resources
                            ;                                :endpoints endpoints})
                            (db-state/set-last-update)])))
      (then (fn [[last-update]]
              (broadcast/send-current-state {:cached-resources {:resources resources
                                                                :endpoints endpoints}
                                             :last-update      last-update})
              (broadcast/send-sync-status :synced)
              (promise-resolve))))
  )

(defn update-cached-resources
  [update-resources-data]
  (logger/debug "Update cached resources" update-resources-data)
  (cache-resources (get-in update-resources-data [:resources] [])
                   (get-in update-resources-data [:endpoints] [])))

(defn get-cached-resources
  []
  (-> (promise-all [(game-cache/get-cached-resources)
                    (vs/get-endpoints)])
      (then (fn [[resources endpoints]]
              {:resources resources
               :endpoints endpoints}))))
