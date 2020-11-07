(ns webchange.service-worker.db.endpoints
  (:require
    [webchange.service-worker.db.core :as core]
    [webchange.service-worker.db.db-course :as db-course]
    [webchange.service-worker.wrappers :refer [then]]))

(def store-name db-course/endpoints-data-store-name)

(defn save-endpoint
  [endpoint]
  (-> (db-course/get-db)
      (then (fn [db]
              (core/add-item db store-name endpoint)))))

(defn get-endpoint
  [pathname]
  (-> (db-course/get-db)
      (then (fn [db]
              (core/get-by-key db store-name pathname)))))
