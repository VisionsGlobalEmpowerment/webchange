(ns webchange.service-worker.db.core
  (:require
    [cljs-idxdb.core :as idx]
    [webchange.service-worker.config :as config]
    [webchange.service-worker.wrappers :refer [promise promise-resolve promise-reject then catch]]))

(def database-version config/release-number)

(defn- get-database-name
  [course-name]
  (str config/app-name "-" course-name))

(defn- event->db [e] (-> e .-target .-result))

(defn- version-change-event
  [e]
  {:old-version (.-oldVersion e)
   :new-version (.-newVersion e)
   :db          (event->db e)})

(defn init-db
  [db-name upgrade-db]
  (promise (fn [resolve reject]
             (let [database-name (get-database-name db-name)
                   request (.open js/indexedDB database-name database-version)]
               (set! (.-onsuccess request) (fn [e]
                                             (resolve (event->db e))))
               (set! (.-onupgradeneeded request) (fn [e]
                                                   (-> (version-change-event e)
                                                       (upgrade-db))))
               (set! (.-onerror request) #(reject "Open indexedDB failed" %))))))


(defn add-item
  ([db store-name data]
   (promise (fn [resolve] (add-item db store-name data resolve))))
  ([db store-name data success-fn]
   (idx/add-item db store-name data (fn [data] (success-fn data)))))

(defn remove-item
  ([db store-name key]
   (promise (fn [resolve] (remove-item db store-name key resolve))))
  ([db store-name key success-fn]
   (let [store (idx/get-tx-store db store-name)
         request (.delete store key)]
     (set! (.-onsuccess request) #(success-fn)))))

(defn get-by-index
  ([db store-name index]
   (promise (fn [resolve] (get-by-index db store-name index resolve))))
  ([db store-name index success-fn]
   (idx/get-by-index db store-name index 0 success-fn)))

(defn find-in-index
  ([db store-name index query]
   (promise (fn [resolve] (find-in-index db store-name index query resolve))))
  ([db store-name index query success-fn]
   (-> (get-by-index db store-name index)
       (then (fn [results]
               (->> results
                    (filter (fn [result] (= query (select-keys result (keys query)))))
                    (success-fn)))))))

(defn get-by-key
  ([db store-name key]
   (promise (fn [resolve] (get-by-key db store-name key resolve))))
  ([db store-name key success-fn]
   (idx/get-by-key db store-name key success-fn)))
