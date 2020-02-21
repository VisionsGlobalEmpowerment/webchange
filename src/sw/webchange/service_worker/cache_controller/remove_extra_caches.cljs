(ns webchange.service-worker.cache-controller.remove-extra-caches
  (:require
    [clojure.string :refer [ends-with? starts-with?]]
    [webchange.service-worker.config :refer [cache-names-prefix release-number]]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.wrappers :refer [cache-delete caches-keys promise-all then]]))

(defn- get-version-info
  [cache-name]
  (let [find-result (re-find #"-(\d*)-([A-Za-z0-9]*$)" cache-name)]
    (if-not (nil? find-result)
      {:course  (get find-result 2)
       :version (js/parseInt (get find-result 1))}
      nil)))


(defn- filter-caches
  [cache-names]
  (let [webchange-cache? (fn [cache-name] (starts-with? cache-name cache-names-prefix))
        old-cache? (fn [cache-name] (-> (get-version-info cache-name) :version (< release-number)))]
    (filter #(and (webchange-cache? %)
                  (old-cache? %)) cache-names)))

(defn remove-extra-caches
  []
  (-> (caches-keys)
      (then (fn [cache-keys]
              (filter-caches cache-keys)))
      (then (fn [filtered-keys]
              (promise-all (map #(do (logger/debug (str "Remove cache: " %))
                                     (cache-delete %))
                                filtered-keys))))))
