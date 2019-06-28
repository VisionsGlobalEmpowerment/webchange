(ns webchange.service-worker
  (:require
    [webchange.service-worker.event-handlers.activate :refer [activate-event-handler]]
    [webchange.service-worker.event-handlers.fetch :refer [fetch-event-handler]]
    [webchange.service-worker.event-handlers.install :refer [install-event-handler]]))

(.addEventListener js/self "install" install-event-handler)
(.addEventListener js/self "activate" activate-event-handler)
(.addEventListener js/self "fetch" fetch-event-handler)
