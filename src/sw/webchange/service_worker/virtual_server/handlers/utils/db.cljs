(ns webchange.service-worker.virtual-server.handlers.utils.db
  (:require
    [cljs-idxdb.core :as idx]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.config :refer [database-name]]
    [webchange.service-worker.wrappers :refer [promise]]))

(def db-version 3)
(def endpoints-data-store-name (str database-name "-endpoints-data"))
(def queue-store-name (str database-name "-requests-queue"))
(def activated-users-store-name (str database-name "-activated-users"))
(def progress-store-name (str database-name "-progress"))
(def data-store-name (str database-name "-data"))

(def db (atom nil))

(defn version-change-event
  [e]
  {:old-version (.-oldVersion e)
   :new-version (.-newVersion e)
   :db (-> e .-target .-result)})

(defn base-event [e] {:db (-> e .-target .-result)})

(defn create-db [name version upgrade-fn success-fn]
  (let [request (.open js/indexedDB name version)]
    (set! (.-onsuccess request) (comp success-fn base-event))
    (set! (.-onupgradeneeded request) (comp upgrade-fn version-change-event))
    (set! (.-onerror request) #(logger/warn "Open indexedDB failed" %))))

(defn upgrade-db
  [{:keys [old-version db]}]
  (when (< old-version 1)
    (-> (idx/create-store db endpoints-data-store-name {:keyPath "endpoint"}))
    (-> (idx/create-store db queue-store-name {:keyPath "date"})
        (idx/create-index "dateIndex" "date" {:unique true})))
  (when (< old-version 2)
    (-> (idx/create-store db activated-users-store-name {:keyPath "code"}))
    (-> (idx/create-store db progress-store-name {:keyPath "id"})))
  (when (< old-version 3)
    (-> (idx/create-store db data-store-name {:keyPath "key"})))
  )

(create-db database-name db-version upgrade-db #(reset! db (:db %)))

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
  [key value success-fn]
  (let [data {:key key :value value}]
    (add-item data-store-name data success-fn)))

(defn get-value
  [key success-fn]
  (get-by-key data-store-name key #(success-fn (:value %))))
