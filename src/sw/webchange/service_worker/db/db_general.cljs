(ns webchange.service-worker.db.db-general
  (:require
    [cljs-idxdb.core :as idx]
    [webchange.service-worker.db.core :as core]
    [webchange.service-worker.wrappers :refer [promise-resolve then]]))

(defonce db (atom nil))

(def state-store-name "state")
(def users-store-name "activated-users")

(defn upgrade-db
  [{:keys [old-version db]}]
  (when (< old-version 1)
    (-> (idx/create-store db state-store-name {:keyPath "key"})))
  (when (< old-version 6)
    (-> (idx/create-store db users-store-name {:keyPath "code"}))))

(defn get-db
  []
  (if (-> @db nil? not)
    (promise-resolve @db)
    (-> (core/init-db "general" upgrade-db)
        (then (fn [db-instance]
                (reset! db db-instance)
                (promise-resolve db-instance))))))
