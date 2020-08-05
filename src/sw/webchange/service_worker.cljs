(ns webchange.service-worker
  (:require
    [webchange.service-worker.event-handlers.activate :as activate]
    [webchange.service-worker.broadcast.core :as broadcast]
    [webchange.service-worker.event-handlers.fetch :as fetch]
    [webchange.service-worker.event-handlers.install :as install]
    [webchange.service-worker.event-handlers.message :as message]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.virtual-server.core :as vs]
    [webchange.service-worker.wrappers :refer [online?]]))

(.addEventListener js/self "unhandledrejection" (fn [error]
                                                  (logger/error "Unhandled Rejection" error)
                                                  (broadcast/send-error "Unhandled Rejection" (.-reason error))))

(.addEventListener js/self "install" install/handle)
(.addEventListener js/self "activate" activate/handle)
(.addEventListener js/self "fetch" fetch/handle)
(.addEventListener js/self "message" message/handle)

(aset (.-connection js/navigator) "onchange" (fn []
                                               (if (online?)
                                                 (do (broadcast/send-sync-status :synced)
                                                     (vs/flush-state))
                                                 (broadcast/send-sync-status :offline))))
