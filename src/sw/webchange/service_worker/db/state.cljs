(ns webchange.service-worker.db.state
  (:require
    [webchange.service-worker.db.core :as core]
    [webchange.service-worker.db.db-course :as db-course]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.wrappers :refer [catch promise-resolve then]]))

(def cached-resources-key "cached-resources")
(def last-update-key "last-update")

(defn- set-value
  [key value]
  (let [data {:key key :value value}]
    (-> (db-course/get-db)
        (then (fn [db]
                (core/add-item db db-course/state-store-name data)))
        (then (fn []
                (promise-resolve value))))))

(defn- get-value
  [key]
  (-> (db-course/get-db)
      (then (fn [db]
              (core/get-by-key db db-course/state-store-name key)))
      (then (fn [result]
              (promise-resolve (:value result))))
      (catch (fn [error]
               (logger/warn (str "Can not get value of " key ":") error)
               (promise-resolve nil)))))

(defn get-last-update
  []
  (get-value last-update-key))

(defn set-last-update
  []
  (let [date (-> (js/Date.) (.toString))]
    (set-value last-update-key date)))

(defn get-cached-resources
  []
  (-> (get-value cached-resources-key)
      (then (fn [resources]
              (or resources {:resources []
                             :endpoints []})))))

(defn set-cached-resources
  [resources]
  (set-value cached-resources-key resources))
