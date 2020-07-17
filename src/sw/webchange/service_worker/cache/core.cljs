(ns webchange.service-worker.cache.core
  (:require
    [clojure.set :refer [difference]]
    [clojure.string :refer [starts-with?]]
    [webchange.service-worker.config :as config]
    [webchange.service-worker.db.general :as general]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.wrappers :refer [cache-add-all cache-delete cache-keys cache-open caches-keys
                                               promise-all promise-resolve request-pathname then catch]]))

(defn get-cache-name
  [key]
  (-> (general/get-current-course)
      (then (fn [current-course]
              (config/get-cache-name key current-course)))))

(defn- get-version-info
  [cache-name]
  (let [find-result (re-find #"-(\d*)-([A-Za-z0-9]*$)" cache-name)]
    (if-not (nil? find-result)
      {:course  (get find-result 2)
       :version (js/parseInt (get find-result 1))}
      nil)))

(defn- filter-caches
  [cache-names cache-name-prefix release-number]
  (let [webchange-cache? (fn [cache-name] (starts-with? cache-name cache-name-prefix))
        old-cache? (fn [cache-name] (-> (get-version-info cache-name) :version (< release-number)))]
    (filter #(and (webchange-cache? %)
                  (old-cache? %)) cache-names)))

(defn remove-old-caches
  [cache-name-prefix release-number]
  (-> (caches-keys)
      (then (fn [cache-keys]
              (filter-caches cache-keys cache-name-prefix release-number)))
      (then (fn [filtered-keys]
              (promise-all (map #(do (logger/debug (str "Remove old cache: " %))
                                     (cache-delete %))
                                filtered-keys))))))

(defn- request->pathname
  [request]
  (-> (request-pathname request)
      (js/decodeURI)))

(defn- get-new-resources
  [path-names cache]
  (-> (cache-keys cache)
      (then (fn [keys] (map request->pathname keys)))
      (then (fn [cached-path-names]
              (->> (difference (set path-names)
                               (set cached-path-names))
                   (into []))))))

(defn- cache-partially
  [cache resources partition]
  (loop [left (vec resources)
         p (promise-resolve nil)]
    (let [limit (min partition (count left))
          current (subvec left 0 limit)
          next (subvec left limit)
          current-p (-> p
                        (then #(cache-add-all cache current))
                        (catch (fn [error]
                                 (logger/error "Caching error" error)
                                 (cache-add-all cache current))))]
      (if (> (count next) 0)
        (recur next current-p)
        current-p))))

(defn cache-resources
  [cache-name resources]
  (logger/debug-folded (str "Resources to cache into " cache-name " (" (count resources) ")") resources)
  (-> (cache-open cache-name)
      (then (fn [cache] (promise-all [(promise-resolve cache)
                                      (get-new-resources resources cache)])))
      (then (fn [[cache new-resources]]
              (let [debug-message (str "Caching new resources into " cache-name " (" (count new-resources) ")")
                    result-promise (cache-partially cache new-resources 5)]
                (logger/debug-folded debug-message new-resources)
                (-> result-promise
                    (then (fn [] (logger/debug (str debug-message " done")))))
                result-promise)))))
