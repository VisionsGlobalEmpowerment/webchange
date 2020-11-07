(ns webchange.service-worker.cache.game
  (:require
    [webchange.service-worker.cache.core :as core]
    [webchange.service-worker.wrappers :refer [then]]))

(defn reset-resources
  ([resources]
   (reset-resources resources {}))
  ([resources options]
  (-> (core/get-cache-name :game)
      (then (fn [cache-name]
              (core/reset-resources cache-name resources options))))))

(defn get-cached-resources
  []
  (-> (core/get-cache-name :game)
      (then (fn [cache-name]
              (core/get-cached-resources cache-name)))))
