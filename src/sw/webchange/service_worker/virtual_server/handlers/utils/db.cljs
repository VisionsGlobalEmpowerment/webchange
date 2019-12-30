(ns webchange.service-worker.virtual-server.handlers.utils.db
  (:require
    [cljs-idxdb.core :as idx]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.config :refer [database-name]]))

(def db-version 2)
(def data-store-name (str database-name "-endpoints-data"))
(def queue-store-name (str database-name "-requests-queue"))
(def activated-users-store-name (str database-name "-activated-users"))
(def progress-store-name (str database-name "-progress"))

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
    (-> (idx/create-store db data-store-name {:keyPath "endpoint"}))
    (-> (idx/create-store db queue-store-name {:keyPath "date"})
        (idx/create-index "dateIndex" "date" {:unique true})))
  (when (< old-version 2)
    (-> (idx/create-store db activated-users-store-name {:keyPath "code"}))
    (-> (idx/create-store db progress-store-name {:keyPath "id"})))
  )

(create-db database-name db-version upgrade-db #(reset! db (:db %)))

(defn add-item
  [store data success-fn]
  (idx/add-item @db store data success-fn))

(defn remove-item
  [store-name key success-fn]
  (let [store (idx/get-tx-store @db store-name)
        request (.delete store key)]
    (set! (.-onsuccess request) #(success-fn))))


;transaction = db.transaction("people", "readwrite");
;objectStore = transaction.objectStore("people");
;request = objectStore.delete(id);
;request.onsuccess = function(evt) {
;                                   console.log("deleted content");
;                                   };


(defn get-by-index
  [store index success-fn]
  (idx/get-by-index @db store index 0 success-fn))

(defn get-by-key
  [store key success-fn]
  (idx/get-by-key @db store key success-fn))
