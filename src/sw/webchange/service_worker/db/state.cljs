(ns webchange.service-worker.db.state
  (:require
    [webchange.service-worker.db.core :as core]
    [webchange.service-worker.db.db-course :as db-course]
    [webchange.service-worker.wrappers :refer [promise-resolve then]]))

(def cached-lessons-key "cached-lessons")
(def current-code-key "current-code")
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
      (then #(:value %))))

(defn get-current-code
  []
  (get-value current-code-key))

(defn set-current-code
  [code]
  (set-value current-code-key code))

(defn get-last-update
  []
  (get-value last-update-key))

(defn set-last-update
  []
  (let [date (-> (js/Date.) (.toString))]
    (set-value last-update-key date)))

(defn get-cached-lessons
  []
  (-> (get-value cached-lessons-key)
      (then (fn [lessons]
              (or lessons [])))))

(defn set-cached-lessons
  [lessons]
  (set-value cached-lessons-key lessons))
