(ns webchange.service-worker.db.db-general
  (:require
    [cljs-idxdb.core :as idx]
    [webchange.service-worker.db.core :as core]
    [webchange.service-worker.wrappers :refer [promise-resolve then]]))

(defonce db (atom nil))

(def state-store-name "state")

(defn upgrade-db
  [{:keys [old-version db]}]
  (when (< old-version 1)
    (-> (idx/create-store db state-store-name {:keyPath "key"}))))

(defn get-db
  []
  (if (-> @db nil? not)
    @db
    (-> (core/init-db "general" upgrade-db)
        (then (fn [db-instance]
                (reset! db db-instance)
                (promise-resolve db-instance))))))
