(ns webchange.service-worker.controllers.web-app-resources
  (:require
    [webchange.service-worker.cache.web-app :as web-app-cache]
    [webchange.service-worker.broadcast.core :as broadcast]
    [webchange.service-worker.db.state :as db-state]
    [webchange.service-worker.requests.api :as api]
    [webchange.service-worker.virtual-server.core :as vs]
    [webchange.service-worker.wrappers :refer [then promise-all]]))

(defn cache-app
  []
  (broadcast/send-sync-status :syncing)
  (-> (api/get-web-app-resources)
      (then (fn [{:keys [resources endpoints]}]
              (promise-all [(web-app-cache/cache-resources resources)
                            (vs/add-endpoints endpoints)])))
      (then (fn []
              (db-state/set-last-update)))
      (then (fn [last-update]
              (.log js/console "last-update" last-update)
              (broadcast/send-current-state {:last-update last-update})
              (broadcast/send-sync-status :synced)))))
