(ns webchange.service-worker.common.db
  (:require
    [cljs-idxdb.core :as idx]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.config :as config]
    [webchange.service-worker.wrappers :refer [promise]]))

(def db (atom nil))
(def database-version config/release-number)

(def data-store-name "data-store")
(def progress-store-name "progress")
(def activated-users-store-name "activated-users")
(def queue-store-name "requests-queue")
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

(defn base-event [e] {:db (-> e .-target .-result)})

(defn upgrade-db
  [{:keys [old-version db]}]
  (when (< old-version 1)
    (-> (idx/create-store db data-store-name {:keyPath "endpoint"}))
    (-> (idx/create-store db queue-store-name {:keyPath "date"})
        (idx/create-index "dateIndex" "date" {:unique true})))
  (when (< old-version 2)
    (-> (idx/create-store db activated-users-store-name {:keyPath "code"}))
    (-> (idx/create-store db progress-store-name {:keyPath "id"})))
  (when (< old-version 3)
    (-> (idx/delete-and-create-store db data-store-name {:keyPath "key"})))
  (when (< old-version 4)
    (-> (idx/create-store db endpoints-data-store-name {:keyPath "endpoint"}))))

(defn init-db
  [course-name]
  (promise (fn [resolve]
             (let [database-name (get-database-name course-name)
                   request (.open js/indexedDB database-name database-version)]
               (set! (.-onsuccess request) (fn [e]
                                             (reset! db (-> e .-target .-result))
                                             (resolve)))
               (set! (.-onupgradeneeded request) (fn [e]
                                                   (-> (version-change-event e)
                                                       (upgrade-db))))
               (set! (.-onerror request) #(logger/warn "Open indexedDB failed" %))))))

(defn add-item
  ([store data success-fn]
   (idx/add-item @db store data success-fn))
  ([store data]
   (promise #(idx/add-item @db store data %))))

(defn remove-item
  [store-name key success-fn]
  (let [store (idx/get-tx-store @db store-name)
        request (.delete store key)]
    (set! (.-onsuccess request) #(success-fn))))

(defn get-by-index
  [store index success-fn]
  (idx/get-by-index @db store index 0 success-fn))

(defn get-by-key
  ([store key success-fn]
   (idx/get-by-key @db store key success-fn))
  ([store key]
   (promise #(idx/get-by-key @db store key %))))

(defn set-value
  ([key value]
   (set-value key value #()))
  ([key value success-fn]
   (let [data {:key key :value value}]
     (add-item data-store-name data success-fn))))

(defn get-value
  [key success-fn]
  (get-by-key data-store-name key #(success-fn (:value %))))

(defn get-current-course
  []
  (promise (fn [resolve]
             (get-value current-course-db-key resolve))))


