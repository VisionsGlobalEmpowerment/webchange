(ns webchange.service-worker.virtual-server.handlers.current-progress
  (:require
    [promesa.core :as p]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.virtual-server.handlers.utils.responses-store :as store]
    [webchange.service-worker.virtual-server.handlers.utils.requests_queue :as queue]
    [webchange.service-worker.virtual-server.handlers.utils.db :as db]
    [webchange.service-worker.virtual-server.handlers.utils.activated-users :as activated-users]
    [webchange.service-worker.wrappers :refer [js-fetch promise-resolve body-json response-clone request-clone then catch data->response]]))

(defn store-current-progress!
  [{progress :progress offline :offline}]
  (let [current-user (activated-users/get-current-user)]
    (-> current-user
        (then #(:id %))
        (then #(db/add-item db/progress-store-name {:progress progress :offline offline :id %})))))

(defn- with-debug
  [x comment]
  (logger/debug "[with-debug]" "[" comment "]" x)
  x)

(defn get-current-progress
  []
  (let [current-user (activated-users/get-current-user)]
    (-> current-user
        (then #(:id %))
        (then #(with-debug % "id"))
        (then #(db/get-by-key db/progress-store-name %))
        (then #(with-debug % "progress")))))

(defn store-body!
  [body offline]
  (let [cloned (.clone body)]
    (-> (body-json cloned)
        (then #(js->clj % :keywordize-keys true))
        (then #(assoc % :offline offline))
        (then store-current-progress!))
    body))

(defn get-offline
  [request]
  (logger/debug "[current-progress] [GET] [offline]")
  (-> (get-current-progress)
      (then data->response)))

(defn post-offline
  [request]
  (logger/debug "[current-progress] [POST] [offline]")
  (store-body! request true)
  (data->response {}))

(defn get-online
  [request]
  (logger/debug "[current-progress] [GET] [online]")
  (let [cloned (request-clone request)]
    (p/let [stored-progress (get-current-progress)
            offline (:offline stored-progress)]
      (if offline
        (get-offline request)
        (-> (js-fetch request)
            (then #(store-body! % false))
            (catch #(get-offline cloned)))
        ))))

(defn post-online
  [request]
  (logger/debug "[current-progress] [POST] [online]")
  (let [cloned (request-clone request)]
    (store-body! request false)
    (-> (js-fetch request)
        (catch #(post-offline cloned)))))

(def handlers {"GET"  {:online  get-online
                       :offline get-offline}
               "POST" {:online  post-online
                       :offline post-offline}})
