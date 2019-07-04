(ns webchange.service-worker.virtual-server.handlers.utils.serializer
  (:require
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.wrappers :refer [promise-resolve response-new
                                               response-headers response-json response-url request-new
                                               request-headers request-json request-url request-method
                                               then]]))

(defn serialize-headers
  [headers]
  (let [iterator (.entries headers)]
    (loop [result {}]
      (let [step (.next iterator)]
        (if (.-done step)
          result
          (let [data (.-value step)
                [key value] data]
            (recur (assoc result key value)))))
      )))

(defn serialize-response
  [response]
  (-> (response-json response)
      (then (fn [body]
              {:body     body
               :headers  (->> response response-headers serialize-headers)}))))

(defn serialize-request
  [request]
  (-> (request-json request)
      (then (fn [body]
              {:url     (request-url request)
               :method  (request-method request)
               :body    (.stringify js/JSON body)
               :headers (->> request request-headers serialize-headers)}))))

(defn deserialize-response
  [{:keys [body headers]}]
  (promise-resolve (response-new (clj->js body) (clj->js headers))))

(defn deserialize-request
  [{:keys [url] :as data}]
  (promise-resolve (request-new url (clj->js data))))
