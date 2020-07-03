(ns webchange.service-worker.cache.game
  (:require
    [webchange.service-worker.cache.core :as core]
    [webchange.service-worker.wrappers :refer [then]]))

(defn cache-resources
  [resources]
  (-> (core/get-cache-name :game)
      (then (fn [cache-name]
              (core/cache-resources cache-name resources)))))
