(ns webchange.service-worker.virtual-server.handlers.current-progress
  (:require
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.virtual-server.handlers.utils.responses-store :as store]
    [webchange.service-worker.virtual-server.handlers.utils.requests_queue :as queue]
    [webchange.service-worker.wrappers :refer [js-fetch promise-resolve response-clone request-clone then]]))

(def store-body-key "current-progress-body")
(def store-headers-key "current-progress-headers")

(defn get-online
  [request]
  (logger/debug "[current-progress] [GET] [online]")
  (-> (queue/flush)
      (then #(js-fetch request))
      (then (fn [response]
              (-> (store/put (response-clone response))
                  (then #(promise-resolve response)))))))

(defn get-offline
  [request]
  (logger/debug "[current-progress] [GET] [offline]")
  (store/take request))

(defn post-online
  [request]
  (logger/debug "[current-progress] [POST] [online]")
  (-> (queue/flush)
      (then #(store/put (request-clone request)))
      (then #(js-fetch request))))

(defn post-offline
  [request]
  (logger/debug "[current-progress] [POST] [offline]")
  (-> (queue/add (request-clone request))
      (then #(store/put (request-clone request)))
      (then #(store/take request))))

(def handlers {"GET"  {:online  get-online
                       :offline get-offline}
               "POST" {:online  post-online
                       :offline post-offline}})
