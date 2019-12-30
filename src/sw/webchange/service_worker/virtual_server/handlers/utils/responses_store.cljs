(ns webchange.service-worker.virtual-server.handlers.utils.responses-store
  (:require
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.virtual-server.handlers.utils.db :as db]
    [webchange.service-worker.virtual-server.handlers.utils.serializer :refer [serialize-response serialize-request
                                                                               deserialize-response]]
    [webchange.service-worker.wrappers :refer [promise promise-all response-pathname request-pathname then]]))

(def store-name db/data-store-name)

(defn- put-to-store
  [object]
  (logger/debug "Put into DB:" (clj->js object))
  (promise #(db/add-item store-name object %)))

(defn- take-from-store
  [pathname]
  (promise #(db/get-by-key store-name pathname %)))

(defn- put-response
  [response]
  (logger/debug "Put response..")
  (-> (serialize-response response)
      (then (fn [serialized]
              (promise (fn [resolve]
                         (db/add-item
                           store-name
                           (merge serialized {:endpoint (response-pathname response)})
                           #(resolve %))))))))

(defn- put-request
  [request]
  (logger/debug "Put request..")
  (-> (promise-all [(serialize-request request)
                    (take-from-store (request-pathname request))])
      (then (fn [[serialized stored-response]]
              (logger/debug "Serialized request:" (clj->js serialized))
              (logger/debug "Stored response:" (clj->js stored-response))
              (put-to-store (assoc stored-response :body (:body serialized)))))))

(defn put
  [entity]
  (logger/debug "Put to responses store:" entity)
  (case (->> entity .-constructor .-name)
    "Request" (put-request entity)
    "Response" (put-response entity)))

(defn take
  [request]
  (logger/debug "Take from responses store:" request)
  (-> (take-from-store (request-pathname request))
      (then deserialize-response)))

(defn swap-item!
  [key f]
  (-> (take-from-store key)
      (then #(put-to-store (f %)))))
