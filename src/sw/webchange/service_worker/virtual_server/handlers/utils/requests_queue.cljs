(ns webchange.service-worker.virtual-server.handlers.utils.requests_queue
  (:require
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.virtual-server.handlers.utils.db :as db]
    [webchange.service-worker.virtual-server.handlers.utils.serializer :refer [serialize-request deserialize-request]]
    [webchange.service-worker.wrappers :refer [js-fetch promise promise-resolve request-url then]]))

(def store-name db/queue-store-name)

(defn- put-to-store
  [object]
  (let [data (assoc object :date (.now js/Date))]
    (logger/debug "Put into DB:" (clj->js data))
    (promise #(db/add-item store-name data %))))

(defn- remove-from-store
  [key]
  (logger/debug "Remove from DB:" key)
  (promise #(db/remove-item store-name key %)))

(defn- take-from-store
  []
  (promise #(db/get-by-index store-name "dateIndex" %)))

(defn- flush-requests
  [requests]
  (logger/debug "Send requests" (clj->js requests))
  (reduce
    (fn [prev-promise request-data]
      (then prev-promise #(-> (deserialize-request request-data)
                              (then (fn [request]
                                      (logger/debug "Flush request:" (:date request-data) request)
                                      (js-fetch request)))
                              (then (fn []
                                      (remove-from-store (:date request-data)))))))
    (promise-resolve true)
    requests))

(defn add
  [request]
  (logger/debug "Add request to queue" request)
  (-> (serialize-request request)
      (then put-to-store)))

(defn flush
  []
  (logger/debug "Flush queued requests..")
  (-> (take-from-store)
      (then flush-requests)))
