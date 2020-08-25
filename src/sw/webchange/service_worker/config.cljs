(ns webchange.service-worker.config
  (:require
    [clojure.string :refer [join]]))

(def app-name "webchange")
(def release-number 10)

(def cache-names-prefix app-name)
(def database-name app-name)

(def api-path "/api/")
(def log-level :debug)

(defn get-cache-name
  ([key]
   (get-cache-name key nil))
  ([key course-name]
   (let [common-caches {:static "static"}
         course-caches {:api  "api"
                        :game "game"}]
     (cond
       (contains? common-caches key) (join "-" [cache-names-prefix (get common-caches key) release-number])
       (contains? course-caches key) (if-not (empty? course-name)
                                       (join "-" [cache-names-prefix (get course-caches key) release-number course-name])
                                       (-> (str "Can not get cache name for <" key ">: Course name is not defined") js/Error. throw))
       :else (-> (str "Cache key <" key "> is not defined") js/Error. throw)))))
