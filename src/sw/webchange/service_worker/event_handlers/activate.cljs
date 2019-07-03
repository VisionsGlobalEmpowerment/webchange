(ns webchange.service-worker.event-handlers.activate
  (:require
    [clojure.string :refer [starts-with?]]
    [webchange.service-worker.config :as config]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.utils :refer [group-promises]]
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

(defn- remove-extra-caches
  [cache-names]
  (->> cache-names
       (filter-caches)
       (map #(do (logger/debug (str "Remove cache: " %))
                 (cache/delete :cache-name %)))
       (group-promises)))

(defn activate-event-handler
  [event]
  (logger/debug "Activate...")
  (.waitUntil event (-> (cache/keys :then remove-extra-caches)
                        (.then #(logger/log "Activation done."))
                        (.catch #(logger/warn "Activation failed." (.-message %))))))
