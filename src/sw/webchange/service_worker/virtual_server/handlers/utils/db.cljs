(ns webchange.service-worker.virtual-server.handlers.utils.db
  (:require
    [cljs-idxdb.core :as idx]
    [webchange.service-worker.config :refer [database-name]]))

(def db-version 1)
(def data-store-name (str database-name "-endpoints-data"))
(def queue-store-name (str database-name "-requests-queue"))

(def db (atom nil))

(idx/create-db database-name db-version
               #(do (-> (idx/create-store % data-store-name {:keyPath "endpoint"}))
                    (-> (idx/create-store % queue-store-name {:keyPath "date"})
                        (idx/create-index "dateIndex" "date" {:unique true})))
               #(reset! db %))

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
