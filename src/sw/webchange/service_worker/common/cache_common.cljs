(ns webchange.service-worker.common.cache-common
  (:require
    [webchange.service-worker.wrappers :refer [promise-resolve]]))

(defn get-current-course
  []
  (promise-resolve "spanish")) ;; ToDo: Fix it
