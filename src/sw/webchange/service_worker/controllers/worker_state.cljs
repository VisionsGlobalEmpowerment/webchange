(ns webchange.service-worker.controllers.worker-state
  (:require
    [webchange.service-worker.db.state :as db-state]
    [webchange.service-worker.config :as config]
    [webchange.service-worker.controllers.game-resources :as game-controller]
    [webchange.service-worker.wrappers :refer [promise-all then]]))

(defn get-current-state
  []
  (-> (promise-all [(db-state/get-last-update)
                    (game-controller/get-cached-resources)])
      (then (fn [[last-update cached-resources]]
              {:version        config/release-number
               :last-update    last-update
               :cached-resources cached-resources}))))
