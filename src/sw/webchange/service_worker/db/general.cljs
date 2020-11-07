(ns webchange.service-worker.db.general
  (:require
    [webchange.service-worker.db.core :as core]
    [webchange.service-worker.db.db-general :as db-general]
    [webchange.service-worker.wrappers :refer [promise promise-resolve then]]))

(def current-course-key "current-course")
(def current-code-key "current-code")

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
  (promise (fn [resolve reject]
             (-> (get-value current-course-key)
                 (then (fn [current-course]
                         (if-not (nil? current-course)
                           (resolve current-course)
                           (reject "Course is not defined"))))))))

(defn set-current-course
  [course]
  (set-value current-course-key course))

(defn get-current-code
  []
  (get-value current-code-key))

(defn set-current-code
  [code]
  (set-value current-code-key code))
