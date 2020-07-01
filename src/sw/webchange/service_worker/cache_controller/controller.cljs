(ns webchange.service-worker.cache-controller.controller
  (:require
    [webchange.service-worker.cache-controller.cache-course :as cache-course]
    [webchange.service-worker.cache-controller.cache-scenes :as cache-scenes]
    [webchange.service-worker.common.cache :refer [get-cached-activity-urls get-cached-activity-endpoints]]
    [webchange.service-worker.common.db :refer [get-value set-value]]
    [webchange.service-worker.config :as config]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.wrappers :refer [promise promise-all promise-resolve promise-reject then]]))

(def current-course-db-key "current-course")

(defn- get-current-course
  []
  (get-value current-course-db-key))

(defn get-current-state
  []
  (-> (get-value current-course-db-key)
      (then (fn [course-name]
              (-> (promise-all [(get-cached-activity-urls course-name)
                                (get-cached-activity-endpoints course-name)
                                (get-value "last-update")])
                  (then (fn [[game-resources game-endpoints last-update]]
                          {:version        config/release-number
                           :last-update    last-update
                           :current-course course-name
                           :game-resources game-resources
                           :game-endpoints game-endpoints})))))))

(defn- set-current-course
  [current-course]
  (promise (fn [resolve]
             (set-value current-course-db-key current-course resolve))))

(defn- set-last-update-date
  []
  (promise (fn [resolve]
             (set-value "last-update" (-> (js/Date.) (.toString)) resolve))))

(defn cache-course
  [course-name]
  (logger/debug "Cache course" course-name)
  (if (nil? course-name)
    (logger/error "Course name is not defined")
    (-> (set-current-course course-name)
        (then (fn []
                (cache-course/cache-course course-name)))
        (then (fn []
                (promise-resolve (set-last-update-date)))))))

(defn cache-scenes
  [data course-name]
  (logger/debug "Update cached scenes")
  (if (nil? course-name)
    (promise-reject (logger/error "Course name is not defined"))
    (cache-scenes/cache-scenes data course-name)))
