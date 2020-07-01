(ns webchange.service-worker.common.cache
  (:require
    [clojure.set :refer [difference]]
    [webchange.service-worker.config :refer [get-cache-name]]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.virtual-server.core :as vs]
    [webchange.service-worker.wrappers :refer [cache-open cache-add-all cache-keys cache-delete-resource
                                               promise-all promise-resolve response-json then catch]]))

(defn- request->pathname
  [request]
  (-> request
      (aget "url")
      (js/URL.)
      (aget "pathname")
      (js/decodeURI)))

(defn- get-cached-pathnames
  [cache]
  (-> (cache-keys cache)
      (then (fn [keys]
              (map request->pathname keys)))))

(defn- get-new-resources
  [pathnames cache]
  (-> (get-cached-pathnames cache)
      (then (fn [cached-pathnames]
              (->> (difference (set pathnames)
                               (set cached-pathnames))
                   (into []))))))

(defn- cache-resources
  [cache-name resources]
  (logger/debug-folded (str "Resources to cache into " cache-name) resources)
  (-> (cache-open cache-name)
      (then (fn [cache]
              (-> (get-new-resources resources cache)
                  (then (fn [new-resources]
                          (logger/debug "Caching" cache-name "resources count: " (count new-resources))
                          (loop [left (vec new-resources)
                                 p (promise-resolve nil)]
                            (let [limit (min 100 (count left))
                                  current (subvec left 0 limit)
                                  next (subvec left limit)
                                  current-p (-> p
                                                (then #(cache-add-all cache current))
                                                (catch (fn [error]
                                                         (logger/error "Caching error" error)
                                                         (cache-add-all cache current))))]
                              (logger/debug "Caching inner count: " (count current))
                              (if (> (count next) 0)
                                (recur next current-p)
                                current-p))))))))))

(defn- remove-caches
  [cache-name resources]
  (logger/debug-folded (str "Resources to remove from " cache-name) resources)
  (-> (cache-open cache-name)
      (then (fn [cache]
              (promise-all (map (fn [url]
                                  (cache-delete-resource cache url))
                                resources))))))

(defn- get-cache-resources
  [cache-name]
  (logger/debug-folded (str "Get cache resources from " cache-name))
  (-> (cache-open cache-name)
      (then (fn [cache]
              (cache-keys cache)))))

(defn- get-cached-activity-resources
  [course-name]
  (get-cache-resources (get-cache-name :game course-name)))

(defn cache-app-resources
  [resources course-name]
  (cache-resources (get-cache-name :static course-name) resources))

(defn cache-game-resources
  [resources course-name]
  (cache-resources (get-cache-name :game course-name) resources))

(defn remove-game-resources
  [resources course-name]
  (remove-caches (get-cache-name :game course-name) resources))

(defn- resources->urls
  [resources]
  (loop [index 0
         result []
         count (or (aget resources "length") 0)]
    (if-not (= index count)
      (recur (inc index)
             (conj result (-> resources
                              (aget index)
                              (request->pathname)))
             count)
      result)))

(defn get-cached-activity-urls
  [course-name]
  (-> (get-cached-activity-resources course-name)
      (then resources->urls)))

(defn get-cached-activity-endpoints
  [course-name]
  (-> (get-cache-resources (get-cache-name :api course-name))
      (then resources->urls)))

(defn cache-endpoints
  [endpoints course-name]
  (vs/add-endpoints endpoints course-name))
