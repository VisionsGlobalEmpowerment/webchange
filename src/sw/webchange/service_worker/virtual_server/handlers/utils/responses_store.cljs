(ns webchange.service-worker.virtual-server.handlers.utils.responses-store
  (:require
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.db.endpoints :as db-endpoints]
    [webchange.service-worker.virtual-server.handlers.utils.serializer :refer [serialize-response serialize-request
                                                                               deserialize-response]]
    [webchange.service-worker.wrappers :refer [promise promise-all response-pathname request-pathname then]]))

(defn- put-response
  [response]
  (logger/debug "Put response..")
  (-> (serialize-response response)
      (then (fn [serialized]
              (db-endpoints/save-endpoint (merge serialized {:endpoint (response-pathname response)}))))))

(defn- put-request
  [request]
  (logger/debug "Put request..")
  (-> (promise-all [(serialize-request request)
                    (db-endpoints/get-endpoint (request-pathname request))])
      (then (fn [[serialized stored-response]]
              (logger/debug "Serialized request:" (clj->js serialized))
              (logger/debug "Stored response:" (clj->js stored-response))
              (db-endpoints/save-endpoint (assoc stored-response :body (:body serialized)))))))

(defn put
  [entity]
  (logger/debug "Put to responses store:" entity)
  (case (->> entity .-constructor .-name)
    "Request" (put-request entity)
    "Response" (put-response entity)))

(defn take-response
  [request]
  (logger/debug "Take from responses store:" request)
  (-> (db-endpoints/get-endpoint (request-pathname request))
      (then deserialize-response)))
