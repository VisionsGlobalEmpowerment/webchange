(ns webchange.service-worker.db.progress
  (:require
    [webchange.service-worker.db.core :as core]
    [webchange.service-worker.db.db-course :as db-course]
    [webchange.service-worker.wrappers :refer [then]]))

(def store-name db-course/progress-store-name)

(defn save-progress
  [progress user offline?]
  (-> (db-course/get-db)
      (then (fn [db]
              (core/add-item db store-name {:progress progress
                                            :offline  offline?
                                            :id       (:id user)})))))

(defn get-progress
  [user]
  (-> (db-course/get-db)
      (then (fn [db]
              (core/get-by-key db store-name (:id user))))))
