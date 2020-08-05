(ns webchange.service-worker.controllers.web-app-resources
  (:require
    [webchange.service-worker.cache.web-app :as web-app-cache]
    [webchange.service-worker.broadcast.core :as broadcast]
    [webchange.service-worker.db.state :as db-state]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.requests.api :as api]
    [webchange.service-worker.virtual-server.core :as vs]
    [webchange.service-worker.wrappers :refer [then promise-all promise-resolve]]))

(defn cache-app
  []
  (broadcast/send-sync-status :syncing)
  (logger/debug "[Cache App] Get web app resources")
  (-> (api/get-web-app-resources)
      (then (fn [{:keys [resources endpoints]}]
              (logger/debug "[Cache App] Cache web app resources")
              (promise-all [(web-app-cache/cache-resources resources)
                            (vs/add-endpoints endpoints)])))
      (then (fn []
              (logger/debug "[Cache App] Set Last update")
              (db-state/set-last-update)))
      (then (fn [last-update]
              (logger/debug "[Cache App] Send current state")
              (broadcast/send-current-state {:last-update last-update})
              (broadcast/send-sync-status :synced)
              (promise-resolve)))))
