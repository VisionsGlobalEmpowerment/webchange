(ns webchange.service-worker.cache-controller.controller
  (:require
    [webchange.service-worker.cache-controller.cache-course :as cache-course]
    [webchange.service-worker.cache-controller.cache-scenes :as cache-scenes]
    [webchange.service-worker.common.broadcast :refer [send-last-update]]
    [webchange.service-worker.common.db :refer [init-db get-value set-value]]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.wrappers :refer [promise promise-resolve then]]))

(def current-course-db-key "current-course")

(defn- get-current-course
  []
  (promise (fn [resolve]
             (get-value current-course-db-key resolve))))

(defn- set-current-course
  [current-course]
  (promise (fn [resolve]
             (set-value current-course-db-key current-course resolve))))

(defn- set-last-update-date
  []
  (let [date (-> (js/Date.) (.toString))]
    (set-value "last-update" date (fn []
                                    (println "last-update 2" date)
                                    (send-last-update date)))
    date))

(defn cache-course
  [course-name]
  (logger/debug "Cache course" course-name)
  (if (nil? course-name)
    (logger/error "Course name is not defined")
    (-> (init-db course-name)
        (then (fn []
                (set-current-course course-name)))
        (then (fn []
                (cache-course/cache-course course-name)))
        (then (fn []
                (promise-resolve (set-last-update-date)))))))

(defn cache-scenes
  [data course-name]
  (logger/debug "Update cached scenes")
  (if (nil? course-name)
    (logger/error "Course name is not defined")
    (cache-scenes/cache-scenes data course-name)))