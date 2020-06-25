(ns webchange.service-worker.common.db
  (:require
    [cljs-idxdb.core :as idx]
    [webchange.service-worker.common.cache-common :as cache-common]
    [webchange.service-worker.config :as config]
    [webchange.service-worker.wrappers :refer [promise promise-resolve promise-reject then catch]]))

(def database-version config/release-number)

(def data-store-name "data-store")
(def progress-store-name "progress")
(def events-store-name "events")
(def activated-users-store-name "activated-users")
(def endpoints-data-store-name "endpoints-data")

(def current-course-db-key "current-course")

(defn- get-database-name
  [course-name]
  (str config/app-name "-" course-name))

(defn- version-change-event
  [e]
  {:old-version (.-oldVersion e)
   :new-version (.-newVersion e)
   :db          (-> e .-target .-result)})

(defn upgrade-db
  [{:keys [old-version db]}]
  (when (< old-version 1)
    (-> (idx/create-store db data-store-name {:keyPath "endpoint"})))
  (when (< old-version 2)
    (-> (idx/create-store db activated-users-store-name {:keyPath "code"}))
    (-> (idx/create-store db progress-store-name {:keyPath "id"})))
  (when (< old-version 3)
    (-> (idx/delete-and-create-store db data-store-name {:keyPath "key"})))
  (when (< old-version 4)
    (-> (idx/create-store db endpoints-data-store-name {:keyPath "endpoint"})))
  (when (< old-version 5)
    (-> (idx/create-store db events-store-name {:keyPath "created"})
        (idx/create-index "user" "user" {:unique false}))))

(def db (atom {:course   ""
               :instance nil}))

(defn- init-db
  [course-name]
  (promise (fn [resolve reject]
             (let [database-name (get-database-name course-name)
                   request (.open js/indexedDB database-name database-version)]
               (set! (.-onsuccess request) (fn [e]
                                             (let [db-instance (-> e .-target .-result)]
                                               (reset! db {:course   course-name
                                                           :instance db-instance})
                                               (resolve (-> e .-target .-result)))))
               (set! (.-onupgradeneeded request) (fn [e]
                                                   (-> (version-change-event e)
                                                       (upgrade-db))))
               (set! (.-onerror request) #(reject "Open indexedDB failed" %))))))

(defn- get-db
  []
  (-> (cache-common/get-current-course)
      (then (fn [current-course]
              (if (and (-> (:instance @db) nil? not)
                       (= (:course @db) current-course))
                (:instance @db)
                (init-db current-course))))))

(defn add-item
  ([store data]
   (promise (fn [resolve] (add-item store data resolve))))
  ([store data success-fn]
   (-> (get-db)
       (then (fn [db] (idx/add-item db store data success-fn))))))

(defn remove-item
  ([store-name key]
  (promise (fn [resolve] (remove-item store-name key resolve))))
  ([store-name key success-fn]
  (-> (get-db)
      (then (fn [db]
              (let [store (idx/get-tx-store db store-name)
                    request (.delete store key)]
                (set! (.-onsuccess request) #(success-fn))))))))

(defn get-by-index
  ([store index]
   (promise (fn [resolve] (get-by-index store index resolve))))
  ([store index success-fn]
   (-> (get-db)
       (then (fn [db]
               (idx/get-by-index db store index 0 success-fn))))))

(defn get-by-key
  [store key]
  (promise (fn [resolve reject]
             (-> (get-db)
                 (then (fn [db]
                         (if db
                           (idx/get-by-key db store key resolve)
                           (reject "DB is not initialized"))))))))

(defn set-value
  ([key value]
   (set-value key value #()))
  ([key value success-fn]
   (let [data {:key key :value value}]
     (add-item data-store-name data success-fn))))

(defn get-value
  [key]
  (-> (get-by-key data-store-name key)
      (then #(:value %))))

(defn get-current-course
  []
  (get-value current-course-db-key))

(defn find-in-index
  ([store index query]
   (promise (fn [resolve] (find-in-index store index query resolve))))
  ([store index query success-fn]
   (-> (get-by-index store index)
       (then (fn [results]
               (->> results
                    (filter (fn [result]
                              (= query (select-keys result (keys query)))))
                    (success-fn)))))))


