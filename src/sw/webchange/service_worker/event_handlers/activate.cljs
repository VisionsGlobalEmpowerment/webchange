(ns webchange.service-worker.event-handlers.activate
  (:require
    [clojure.string :refer [starts-with?]]
    [webchange.service-worker.config :as config]
    [webchange.service-worker.utils :refer [log]]
    [webchange.wrappers.cache :as cache]))

(defn- filter-caches
  [cache-names]
  (let [webchange-cache? #(starts-with? % config/cache-names-prefix)
        cache-not-used? (fn [cache-name]
                          (->> (vals config/cache-names)
                               (some #(= cache-name %))
                               (not)))]
    (filter #(and (webchange-cache? %)
                  (cache-not-used? %)) cache-names)))

(defn- wrap-to-promise
  [results]
  (->> results
       (clj->js)
       (js/Promise.all)))

(defn- remove-extra-caches
  [cache-names]
  (->> cache-names
       (filter-caches)
       (map #(cache/delete :cache-name %))
       (wrap-to-promise)))

(defn activate-event-handler
  [event]
  (log "Activate..")
  (.waitUntil event (cache/keys :then remove-extra-caches)))
