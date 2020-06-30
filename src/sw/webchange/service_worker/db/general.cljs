(ns webchange.service-worker.db.general
  (:require
    [webchange.service-worker.db.core :as core]
    [webchange.service-worker.db.db-general :as db-general]
    [webchange.service-worker.wrappers :refer [promise-resolve then]]))

(def current-course-key "current-course")

(defn- set-value
  [key value]
  (let [data {:key key :value value}]
    (-> (db-general/get-db)
        (then (fn [db]
                (core/add-item db db-general/state-store-name data))))))

(defn- get-value
  [key]
  (-> (db-general/get-db)
      (then (fn [db]
              (core/get-by-key db db-general/state-store-name key)))
      (then (fn [data]
              (promise-resolve (:value data))))))

(defn get-current-course
  []
  ;(println "get-current-course")
  ;(get-value current-course-key)
  (promise-resolve "spanish")
  )

(defn set-current-course
  [code]
  ;(set-value current-course-key code)
  (promise-resolve "spanish")
  )
