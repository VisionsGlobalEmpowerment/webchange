(ns webchange.service-worker.controllers.worker-state
  (:require
    [webchange.service-worker.db.state :as db-state]
    [webchange.service-worker.config :as config]
    [webchange.service-worker.wrappers :refer [promise-all then]]))

(defn get-current-state
  []
  (-> (promise-all [(db-state/get-last-update)
                    (db-state/get-cached-lessons)])
      (then (fn [[last-update cached-lessons]]
              {:version        config/release-number
               :last-update    last-update
               :cached-lessons cached-lessons}))))
