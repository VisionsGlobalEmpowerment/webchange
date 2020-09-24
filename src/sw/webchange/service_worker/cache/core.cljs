(ns webchange.service-worker.cache.core
  (:require
    [clojure.set :refer [difference]]
    [clojure.string :refer [starts-with?]]
    [webchange.service-worker.config :as config]
    [webchange.service-worker.db.general :as general]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.wrappers :refer [cache-add-all cache-delete cache-keys cache-open caches-keys cache-delete-resource
                                               promise-all promise-resolve request-pathname then catch]]))

(defn get-cache-name
  [key]
  (-> (general/get-current-course)
      (then (fn [current-course]
              (config/get-cache-name key current-course)))))

(defn- get-version-info
  [cache-name]
  (let [find-result (re-find #"-(\d*)(?:-[A-Za-z]*)?$" cache-name)]
    (if-not (nil? find-result)
      {:version (js/parseInt (get find-result 1))}
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

(defn cache->cached-resources
  [cache]
  (-> (cache-keys cache)
      (then (fn [keys]
              (->> keys
                   (map request-pathname)
                   (map js/decodeURI))))))

(defn get-cached-resources
  [cache-name]
  (-> (cache-open cache-name)
      (then cache->cached-resources)))

(defn- diff
  [list1 list2]
  (->> (difference (set list1)
                   (set list2))
       (into [])))

(defn- get-diff-resources
  [path-names cache]
  (-> (cache->cached-resources cache)
      (then (fn [cached-path-names]
              (let [path-names (map js/encodeURI path-names)
                    cached-path-names (map js/encodeURI cached-path-names)]
                {:add    (diff path-names cached-path-names)
                 :remove (diff cached-path-names path-names)})))))

(defn- cache-partially
  ([cache resources partition]
   (cache-partially cache resources partition {}))
  ([cache resources partition {:keys [on-progress]
                               :or   {on-progress #()}}]
   (loop [left (vec resources)
          p (promise-resolve nil)]
     (let [limit (min partition (count left))
           current (subvec left 0 limit)
           next (subvec left limit)
           current-p (-> p
                         (then (fn []
                                 (on-progress (- 1 (/ (count left)
                                                      (count resources))))
                                 (cache-add-all cache current)))
                         (catch (fn [error]
                                  (logger/error "Caching error" error)
                                  (cache-add-all cache current))))]
       (if (> (count next) 0)
         (recur next current-p)
         current-p)))))

(defn- remove-resources
  [cache path-names]
  (loop [[current & next] (vec path-names)
         p (promise-resolve nil)]
    (let [current-p (-> p
                        (then #(cache-delete-resource cache current))
                        (catch (fn [error]
                                 (logger/error "Caching error" error)
                                 (cache-delete-resource cache current))))]
      (if (> (count next) 0)
        (recur next current-p)
        current-p))))

(defn reset-resources
  ([cache-name resources]
   (reset-resources cache-name resources {}))
  ([cache-name resources options]
  (logger/debug-folded (str "Reset cache " cache-name " resources (" (count resources) ")") resources)
  (-> (cache-open cache-name)
      (then (fn [cache] (promise-all [(promise-resolve cache)
                                      (get-diff-resources resources cache)])))
      (then (fn [[cache {new-resources      :add
                         outdated-resources :remove}]]
              (logger/debug-folded (str "Removing outdated resources from " cache-name " (" (count outdated-resources) ")") outdated-resources)
              (-> (remove-resources cache outdated-resources)
                  (then (fn []
                          (logger/debug-folded (str "Caching new resources into " cache-name " (" (count new-resources) ")") new-resources)
                          (cache-partially cache new-resources 5 options)))
                  (then (fn []
                          (logger/debug (str "Reset cache resources done"))
                          (promise-resolve)))))))))
