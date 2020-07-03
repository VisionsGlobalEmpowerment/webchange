(ns webchange.service-worker.db.events
  (:require
    [webchange.service-worker.db.core :as core]
    [webchange.service-worker.db.db-course :as db-course]
    [webchange.service-worker.wrappers :refer [then]]))

(def store-name db-course/events-store-name)

(defn save-event
  [event user]
  (-> (db-course/get-db)
      (then (fn [db]
              (core/add-item db store-name {:data    event
                                            :user    (:id user)
                                            :created (:created-at event)})))))

(defn get-events
  [user]
  (-> (db-course/get-db)
      (then (fn [db]
              (core/find-in-index db store-name "user" {:user (:id user)})))))

(defn remove-by-date
  [date]
  (-> (db-course/get-db)
      (then (fn [db]
              (core/remove-item db store-name date)))))
