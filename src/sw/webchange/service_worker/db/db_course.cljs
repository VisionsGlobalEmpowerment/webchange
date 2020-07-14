(ns webchange.service-worker.db.db-course
  (:require
    [cljs-idxdb.core :as idx]
    [webchange.service-worker.db.general :as general]
    [webchange.service-worker.db.core :as core]
    [webchange.service-worker.wrappers :refer [promise-resolve then]]))

(defonce db (atom {:course   ""
                   :instance nil}))

(def progress-store-name "progress")
(def events-store-name "events")
(def endpoints-data-store-name "endpoints-data")
(def state-store-name "state")
(def users-store-name "activated-users")

(defn upgrade-db
  [{:keys [old-version db]}]
  (when (< old-version 1)
    (-> (idx/create-store db state-store-name {:keyPath "endpoint"})))
  (when (< old-version 2)
    (-> (idx/create-store db users-store-name {:keyPath "code"}))
    (-> (idx/create-store db progress-store-name {:keyPath "id"})))
  (when (< old-version 3)
    (-> (idx/delete-and-create-store db state-store-name {:keyPath "key"})))
  (when (< old-version 4)
    (-> (idx/create-store db endpoints-data-store-name {:keyPath "endpoint"})))
  (when (< old-version 5)
    (-> (idx/create-store db events-store-name {:keyPath "created"})
        (idx/create-index "user" "user" {:unique false}))))

(defn get-db
  []
  (-> (general/get-current-course)
      (then (fn [current-course]
              (if (and (-> (:instance @db) nil? not)
                       (= (:course @db) current-course))
                (:instance @db)
                (-> (core/init-db current-course upgrade-db)
                    (then (fn [db-instance]
                            (reset! db {:course   current-course
                                        :instance db-instance})
                            (promise-resolve db-instance)))))))))

