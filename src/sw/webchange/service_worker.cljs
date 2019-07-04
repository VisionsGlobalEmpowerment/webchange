(ns webchange.service-worker
  (:require
    [webchange.service-worker.event-handlers.activate :as activate]
    [webchange.service-worker.event-handlers.fetch :as fetch]
    [webchange.service-worker.event-handlers.install :as install]))

(.addEventListener js/self "install" install/handle)
(.addEventListener js/self "activate" activate/handle)
(.addEventListener js/self "fetch" fetch/handle)
